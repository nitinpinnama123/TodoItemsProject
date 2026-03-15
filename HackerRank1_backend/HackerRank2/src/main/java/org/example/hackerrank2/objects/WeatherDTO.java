package org.example.hackerrank2.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeatherDTO {
    private String cityName;
    private String region;
    private Double latitude;
    private Double longitude;
    private Double temperature;
    private Long timestamp;
}