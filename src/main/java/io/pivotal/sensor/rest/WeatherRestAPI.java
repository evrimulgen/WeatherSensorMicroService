package io.pivotal.sensor.rest;

import io.pivotal.sensor.model.Weather;
import io.pivotal.sensor.service.WeatherSensorService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample requests:
 * http://localhost:8080/get-weather-by-sensorid/need-a-sensor-id
 * http://localhost:8080/get-all-weather-readings-by-sensorid/need-a-sensor-id/between/2014-06-12T00:00:00.000Z/2015-11-12T00:00:00.000Z
 * 
 * @author belle
 *
 */
@RestController
public class WeatherRestAPI {

	@Autowired
	private WeatherSensorService weatherSensorService;
	
	//http://localhost:8080/get-weather-by-sensorid/99
	@RequestMapping(value="/get-weather-by-sensorid-ms/{sensorID}", method=RequestMethod.GET)
	public List<Weather> findBySensorID(@PathVariable String sensorID) {
		return weatherSensorService.findBySensorID(sensorID);
	}

	//http://localhost:8080/get-all-weather-readings-by-sensorid/33/between/2014-06-12T00:00:00.000Z/2014-11-12T00:00:00.000Z
	@RequestMapping(value="/get-all-weather-readings-by-sensorid-ms/{sensorID}/between/{startDate}/{endDate}", method=RequestMethod.GET)
	public List<Weather> findWeatherSensorReadingBetween(@PathVariable String sensorID, @PathVariable  @DateTimeFormat(iso = ISO.DATE) Date startDate, @PathVariable  @DateTimeFormat(iso = ISO.DATE) Date endDate) {
		return weatherSensorService.findWeatherSensorReading(sensorID, startDate, endDate);
	}
	
}
