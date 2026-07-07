package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.models.entities.Email;
import ar.utn.ba.ddsi.mailing.models.repositories.IEmailRepository;
import ar.utn.ba.ddsi.mailing.services.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService implements IEmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final IEmailRepository emailRepository;

    JavaMailSender javaMailSender;

    public EmailService(IEmailRepository emailRepository, JavaMailSender javaMailSender) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Email crearEmail(Email email) {
        return emailRepository.save(email);
    }

    @Override
    public List<Email> obtenerEmails(Boolean pendiente) {
        if (pendiente != null) {

            return emailRepository.findByEnviado(!pendiente);
        }
        return emailRepository.findAll();
    }

    @Override
    public void procesarPendientes() {
        List<Email> pendientes = emailRepository.findByEnviado(false);
        for (Email email : pendientes) {

            try
            {
                MimeMessage mensaje = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
                helper.setTo(email.getDestinatario());
                helper.setText(email.getContenido());
                helper.setFrom(email.getRemitente());
                mensaje.setSubject("Notificacion de clima");
                javaMailSender.send(mensaje);
            }
            catch(MessagingException e)
            {
                throw new RuntimeException(e);
            }
            email.setEnviado(true);
            emailRepository.save(email);
        }
    }

    @Override
    public void loguearEmailsPendientes() {
        List<Email> pendientes = obtenerEmails(true);
        logger.info("Emails pendientes de envío: {}", pendientes.size());
        pendientes.forEach(email -> 
            logger.info("Email pendiente - ID: {}, Destinatario: {}, Asunto: {}", 
                email.getId(),
                email.getDestinatario(), 
                email.getAsunto())
        );
    }
} 