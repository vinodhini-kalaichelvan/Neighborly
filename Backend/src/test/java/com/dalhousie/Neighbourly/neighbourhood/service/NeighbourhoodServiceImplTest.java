package com.dalhousie.Neighbourly.neighbourhood.service;

import com.dalhousie.Neighbourly.neighbourhood.entity.Neighbourhood;
import com.dalhousie.Neighbourly.neighbourhood.repository.NeighbourhoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NeighbourhoodServiceImplTest {

    @Mock
    private NeighbourhoodRepository neighbourhoodRepository;

    @InjectMocks
    private NeighbourhoodServiceImpl neighbourhoodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsNeighbourhoodExist_true() {
        int neighbourhoodId = 1;
        Neighbourhood mockNeighbourhood = new Neighbourhood();
        mockNeighbourhood.setNeighbourhoodId(neighbourhoodId);

        when(neighbourhoodRepository.findByNeighbourhoodId(neighbourhoodId)).thenReturn(Optional.of(mockNeighbourhood));

        boolean result = neighbourhoodService.isNeighbourhoodExist(neighbourhoodId);
        assertTrue(result);

        verify(neighbourhoodRepository).findByNeighbourhoodId(neighbourhoodId);
    }

    @Test
    void testIsNeighbourhoodExist_false() {
        int neighbourhoodId = 999;

        when(neighbourhoodRepository.findByNeighbourhoodId(neighbourhoodId)).thenReturn(Optional.empty());

        boolean result = neighbourhoodService.isNeighbourhoodExist(neighbourhoodId);
        assertFalse(result);
        verify(neighbourhoodRepository).findByNeighbourhoodId(neighbourhoodId);
    }
}
