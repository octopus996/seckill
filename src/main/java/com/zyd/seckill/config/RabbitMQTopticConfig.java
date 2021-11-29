/*

package com.zyd.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQTopticConfig {
    private static final String QUEUE01="queue_topic01";
    private static final String QUEUE02="queue_topic02";
    private static final String EXCHANGE="exchang_topic";
    private static final String ROUTING_TOPIC01="*.queue.#";
    private static final String ROUTING_TOPIC02="#.queue.#";

    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding bind01(){
        return BindingBuilder.bind(queue01()).to(exchange()).with(ROUTING_TOPIC01);
    }
    @Bean
    public Binding bind02(){
        return BindingBuilder.bind(queue02()).to(exchange()).with(ROUTING_TOPIC02);
    }

}
*/
