/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.stereotype.Component;

/**
 *
 * @author Austunner
 */

@Component
public class QueueReceiveManager implements MessageListener
{
    public void onMessage( final Message message )
    {
        if ( message instanceof TextMessage )
        {
            final TextMessage textMessage = (TextMessage) message;
            try
            {
                System.out.println( textMessage.getText() );
            }
            catch (final JMSException e)
            {
                e.printStackTrace();
            }
        }
    }
}
