


package com.zyd.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDirectConfig {
    private static final String QUEUE_DIRECT01="queue_direct01";
    private static final String QUEUE_DIRECT02="queue_direct02";
    private static final String EXCHANG_DIRECT="exchang_direct";
    private static final String ROUTINGKEY01="routingkey01";
    private static final String ROUTINGKEY02="routingkey02";

    @Bean
    public Queue queue_direct01(){
        return new Queue(QUEUE_DIRECT01);
    }
    @Bean
    public Queue queue_direct02(){
        return new Queue(QUEUE_DIRECT02);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANG_DIRECT);
    }
    @Bean
    public Binding bind_direct01(){
        return BindingBuilder.bind(queue_direct01()).to(directExchange()).with(ROUTINGKEY01);
    }
    @Bean
    public Binding bind_direct02(){
        return BindingBuilder.bind(queue_direct02()).to(directExchange()).with(ROUTINGKEY02);
    }
}


