package br.com.authenticator.inbound;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("authenticator")
public class TestController {

    private final EmailGateway emailGateway;

    @PostMapping("test")
    public ResponseEntity<String> teste(@RequestBody Email email) {
        String retorno = emailGateway.enviar(email.getFrom(), email.getTo(), email.getSubject(), email.getText());
        return ResponseEntity.ok(retorno);
    }

}
