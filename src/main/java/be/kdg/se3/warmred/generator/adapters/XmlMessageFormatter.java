package be.kdg.se3.warmred.generator.adapters;

import be.kdg.se3.warmred.generator.domain.CancelOrderMessage;
import be.kdg.se3.warmred.generator.domain.CreateOrderMessage;
import be.kdg.se3.warmred.generator.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class XmlMessageFormatter implements MessageFormatter {
    private final Logger logger = LoggerFactory.getLogger(XmlMessageFormatter.class);

    @Override
    public String format(Message message) {
        String xmlResult = "Default xml result";
        try {
            JAXBContext context;
            Marshaller marshaller;
            StringWriter stringWriter = new StringWriter();

            if (message instanceof CreateOrderMessage) {
                logger.info("The message to format is of type:'CreateOrderMessage'");
                context = JAXBContext.newInstance(CreateOrderMessage.class);
            } else if (message instanceof CancelOrderMessage) {
                logger.info("The message to format is of type:'CancelOrderMessage'");
                context = JAXBContext.newInstance(CancelOrderMessage.class);
            } else {
                throw new FormatterException("The given instance has not been added to the XmlMessageFormatter");
            }

            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(message, stringWriter);
            xmlResult = stringWriter.toString();
        } catch (JAXBException e) {
            logger.error("Something went wrong with the conversion from message to xml, a JAXB exception occurred");
        } catch (FormatterException e) {
            logger.error("The given instance has not been added to the XmlMessageFormatter");
        }
        return xmlResult;
    }
}
