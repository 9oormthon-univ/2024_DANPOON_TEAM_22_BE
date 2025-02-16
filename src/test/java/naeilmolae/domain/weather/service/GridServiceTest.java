package naeilmolae.domain.weather.service;

import naeilmolae.domain.weather.domain.Grid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GridServiceTest {
    @Autowired
    private GridService gridService;

    @Test
    void getGridCoordinates() {
        // given
        Double latitude = 37.5665;
        Double longitude = 126.9780;

        // when
        Grid grid = gridService.getGridCoordinates(latitude, longitude);
//
//        // then
//        assertNotNull(grid);
//        assertEquals(grid.getX(), 60);
//        assertEquals(grid.getY(), 127);
    }
}