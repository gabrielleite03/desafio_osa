package br.com.kenjix.services;

import br.com.kenjix.data.dto.AgencyLocationDTO;
import br.com.kenjix.model.AgencyLocation;
import br.com.kenjix.repository.AgencyLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static br.com.kenjix.mapper.ObjectMapper.parseObject;

@Service
public class AgencyService {
    private final Logger LOGGER = LoggerFactory.getLogger(AgencyService.class);

    private AgencyLocationRepository repository;

    public AgencyService(AgencyLocationRepository repository) {
        this.repository = repository;
    }

    public void register(AgencyLocationDTO agencyLocationDTO) {
        AgencyLocation location = parseObject(agencyLocationDTO, AgencyLocation.class);
        repository.save(location);
        LOGGER.info("Agência cadastrada com sucesso!");
    }

    public Map<String, String> distancy(double posX, double posY) {
        PageRequest limit = PageRequest.of(0, 3);
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
