package br.com.kenjix.controllers;

import br.com.kenjix.data.dto.AgencyLocationDTO;
import br.com.kenjix.services.AgencyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/desafio")
@Tag(name = "Agency", description = "Endpoints for Managing Agencies")
public class AgencyController {
    private final AgencyService agencyService;

    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @PostMapping(value = "/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(
            @RequestBody AgencyLocationDTO agencyLocationDTO) {
        agencyService.register(agencyLocationDTO);
        return new ResponseEntity<>("Agency Location regitered successfully!", HttpStatus.OK);
    }
    @GetMapping(value = "/distancia",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> distance(
            @RequestParam(value = "posX") double posX,
            @RequestParam(value = "posY") double posY
    ){

    return ResponseEntity.ok(agencyService.distancy(posX, posY));
    }


}
