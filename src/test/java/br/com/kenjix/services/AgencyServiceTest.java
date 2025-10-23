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

            AgencyService spyService = spy(agencyService);
            doReturn(location).when(spyService).register(dto);

            spyService.register(dto);

            verify(repository, times(1)).save(location);
    }

    @Test
    void distancyShouldBeOK() {
        AgencyLocation a1 = new AgencyLocation();
        a1.setId(1L);
        a1.setPosX(Double.valueOf(0));
        a1.setPosY(Double.valueOf(0));

        AgencyLocation a2 = new AgencyLocation();
        a2.setId(2L);
        a2.setPosX(Double.valueOf(3));
        a2.setPosY(Double.valueOf(4));

        List<AgencyLocation> mockList = List.of(a1, a2);

        when(repository.findAgencyNearest(0.0, 0.0, PageRequest.of(0, 3)))
                .thenReturn(mockList);

        Map<String, String> resultado = agencyService.distancy(0, 0);

        assertEquals(2, resultado.size());
        assertEquals("distância = 0", resultado.get("AGENCIA_1"));
        assertEquals("distância = 5", resultado.get("AGENCIA_2"));

        verify(repository, times(1)).findAgencyNearest(0.0, 0.0, PageRequest.of(0, 3));


    }


}