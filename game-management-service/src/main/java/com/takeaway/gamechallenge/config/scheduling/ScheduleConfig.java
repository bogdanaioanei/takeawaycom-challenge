package com.takeaway.gamechallenge.config.scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.takeaway.gamechallenge.config.Profiles.TEST;

@Configuration
@EnableScheduling
@Profile("!" + TEST)
public class ScheduleConfig {

}
