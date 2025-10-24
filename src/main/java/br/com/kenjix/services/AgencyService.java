package br.com.kenjix.services;

import br.com.kenjix.data.dto.AgencyLocationDTO;
import br.com.kenjix.model.AgencyLocation;
import br.com.kenjix.repository.AgencyLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static br.com.kenjix.mapper.ObjectMapper.parseObject;

@Service
public class AgencyService {
    private static final String CACHE_NAME = "agency";
    private static final String HITS_HASH = "cache_hits";
    private static final int MAX_HITS = 10;
    private final Logger LOGGER = LoggerFactory.getLogger(AgencyService.class);
    private final AgencyLocationRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public AgencyService(AgencyLocationRepository repository) {
        this.repository = repository;
    }

    public void register(AgencyLocationDTO agencyLocationDTO) {
        AgencyLocation location = parseObject(agencyLocationDTO, AgencyLocation.class);
        repository.save(location);
        LOGGER.info("Agência cadastrada com sucesso!");
    }


    public Map<String, String> distance(double posX, double posY) {


        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String field = "position:" + posX + ":" + posY;
        String cacheKey = String.format("%s_%s", CACHE_NAME, field);
        int hits = hashOps.increment(HITS_HASH, field, 1).intValue();
        if (hits ==1  ) {

            LOGGER.info("Cache renovado após "+hits+" consultas para: " + field);
            var result = distanceDefault(posX, posY);
            redisTemplate.opsForValue().set(cacheKey, result);
            return result;

        } else if(hits >= MAX_HITS){
            LOGGER.info("Cache renovado após "+hits+" consultas para: " + field);
            hashOps.delete(HITS_HASH, field);
            var result = distanceDefault(posX, posY);
            redisTemplate.opsForValue().set(cacheKey, result);
            return result;
        }else {
            var result = (Map<String, String>) getValueByKey(cacheKey);
            redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
            return  result;
        }



    }

    public Object getValueByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Cacheable(value = CACHE_NAME, key = "#posX + ':' + #posY")
    private Map<String, String> distanceCachable(double posX, double posY) {
        LOGGER.info("Realizando consulta a base de dados.");
        PageRequest limit = PageRequest.of(0, 10);
        List<AgencyLocation> locations = repository.findAgencyNearest(posX, posY, limit);

        Map<String, String> agencies = new LinkedHashMap<>();
        for (AgencyLocation al : locations) {
            var distance = calculateDistance(al.getPosX(), al.getPosY(), posX, posY);
            agencies.put(
                    String.format("AGENCIA_%d", al.getId()),
                    String.format("distância = %s", roundPrecision(distance, 2)));
        }

        return agencies;
    }

    private Map<String, String> distanceDefault(double posX, double posY) {
        PageRequest limit = PageRequest.of(0, 10);
        List<AgencyLocation> locations = repository.findAgencyNearest(posX, posY, limit);

        Map<String, String> agencies = new LinkedHashMap<>();
        for (AgencyLocation al : locations) {
            var distance = calculateDistance(al.getPosX(), al.getPosY(), posX, posY);
            agencies.put(
                    String.format("AGENCIA_%d", al.getId()),
                    String.format("distância = %s", roundPrecision(distance, 2)));
        }

        return agencies;
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private String roundPrecision(double valor, int casasDecimais) {
        String padrao = "#." + "#".repeat(casasDecimais);
        DecimalFormat df = new DecimalFormat(padrao);
        return df.format(valor);
    }
}
