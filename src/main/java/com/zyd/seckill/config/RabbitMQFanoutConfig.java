/*





package com.zyd.seckill.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQFanoutConfig {
    private static final String QUEUE01="queue_fanout01";
    private static final String QUEUE02="queue_fanout02";
    private static final String EXCHANG_FANOUT="fanoutExchang";

    @Bean
    public Queue queue(){
        return new Queue("queue",true);
    }

    @Bean
    public Queue queue_fanout01(){
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue_fanout02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public FanoutExchange exchange_fanout(){
        return new FanoutExchange(EXCHANG_FANOUT);
    }
    @Bean
    public Binding bing_fanout01(){
        return BindingBuilder.bind(queue_fanout01()).to(exchange_fanout());
    }
    @Bean
    public Binding bing_fanout02(){
        return BindingBuilder.bind(queue_fanout02()).to(exchange_fanout());
    }
}




*/
