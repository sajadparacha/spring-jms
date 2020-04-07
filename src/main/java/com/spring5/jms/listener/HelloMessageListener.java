package com.spring5.jms.listener;


import com.spring5.jms.config.JmsConfig;
import com.spring5.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

/**
 * Created by jt on 2019-07-17.
 */
//@Component
@RequiredArgsConstructor
public class HelloMessageListener {
    private final JmsTemplate jmsTemplate;
    //**Listen for a message in the que named "my-hello-world"
    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       /*
                       * We are using below properties just for teh sake of looking deep into what is going on
                       * However for the sake of this simple example we can just comment below attributes since all we want is just the Message here
                       *
                       * */
                       @Headers MessageHeaders headers, Message message){

        System.out.println("I Got a Message!!!!!");

        System.out.println(helloWorldMessage);


        // uncomment and view to see retry count in debugger
        /*
        * JMS is transactional in nature , i.e if we uncomment the below line , we are actually receiving the message from
        * the que but since we are trowing an exception the transaction is not completed , so the message
        *  we be requeued and resent again.
        *This is a very important feature , because unlike other protocols JMS actually tries to make sure a message was completly delivered
        * and if not it try to do it again.
        * to demostrate that , uncomment below line and debug the code , try to click play 6 times and the see the
        * headers object and noticed jms_redelivered and JMXDeliveryCount properties.
        * */
//         throw new RuntimeException("foo");

    }
    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage,
                               @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage payloadMsg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("World!!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMsg);

    }

}