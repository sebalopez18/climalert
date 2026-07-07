package ar.utn.ba.ddsi.mailing.models.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Email {
    private Long id;
    private String destinatario;
    private String remitente;
    private String asunto;
    private String contenido;
    private boolean enviado;

    public Email(String destinatario, String remitente, String asunto, String contenido) {
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.asunto = asunto;
        this.contenido = contenido;
        this.enviado = false;
    }

    //No uso este metodo ya que el envio se cubre en el service

    /*
    public void enviar() {
        //TODO: Implementación pendiente. Podríamos usar adapters
    }*/
} 