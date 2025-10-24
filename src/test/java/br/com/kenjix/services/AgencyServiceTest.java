package br.com.kenjix.services;

import br.com.kenjix.data.dto.AgencyLocationDTO;
import br.com.kenjix.model.AgencyLocation;
import br.com.kenjix.repository.AgencyLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class AgencyServiceTest {
    @InjectMocks
    private AgencyService agencyService;

    @Mock
    private AgencyLocationRepository repository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerShouldBeOK() {
        AgencyLocationDTO dto = new AgencyLocationDTO();
        dto.setPosX(10.0);
        dto.setPosY(20.0);

        AgencyLocation location = new AgencyLocation();
        location.setPosX(dto.getPosX());
        location.setPosY(dto.getPosY());

        agencyService.register(dto);
    }

    @Test
    void distanceShouldBeOK() {
        AgencyLocation a1 = new AgencyLocation();
        a1.setId(1L);
        a1.setPosX(Double.valueOf(0));
        a1.setPosY(Double.valueOf(0));

        AgencyLocation a2 = new AgencyLocation();
        a2.setId(2L);
        a2.setPosX(Double.valueOf(3));
        a2.setPosY(Double.valueOf(4));

        List<AgencyLocation> mockList = List.of(a1, a2);


        String field = "position:" + 0.0 + ":" + 0.0;
        String cacheKey = String.format("%s_%s", "agency", field);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        redisTemplate.opsForHash().put("cache_hits", field, 1);
        verify(hashOperations).put("cache_hits", field, 1);

        Map<String, String> agencies = new LinkedHashMap<>();
        agencies.put(
                String.format("AGENCIA_%d", 1),
                String.format("distância = %s", "0"));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(cacheKey)).thenReturn(agencies);




        Map<String, String> resultado = agencyService.distance(0, 0);

        assertEquals(1, resultado.size());
        assertEquals("distância = 0", resultado.get("AGENCIA_1"));


    }


}