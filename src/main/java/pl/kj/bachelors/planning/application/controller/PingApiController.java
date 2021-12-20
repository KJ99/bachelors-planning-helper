package pl.kj.bachelors.planning.application.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ping")
@Tag(name = "Health")
public class PingApiController {
    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<?> ping() {
        template.convertAndSend("/foo", "Hello");
        return ResponseEntity.noContent().build();
    }

}
