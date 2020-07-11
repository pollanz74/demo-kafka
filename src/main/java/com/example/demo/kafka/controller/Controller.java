/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo.kafka.controller;

import com.example.demo.kafka.domain.Foo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.StringUtils.commaDelimitedListToSet;

@RestController
public class Controller {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @Value("${demo.foo.command.topic.name}")
    private String topicName;

    @PostMapping(path = "/send/foos/{what}")
    public void sendFoo(@PathVariable String what) {
        commaDelimitedListToSet(what)
                .stream()
                .map(Foo::new)
                .forEach(foo -> template.send(topicName, foo));
    }

}
