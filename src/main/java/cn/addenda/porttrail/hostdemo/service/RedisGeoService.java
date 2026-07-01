package cn.addenda.porttrail.hostdemo.service;

import cn.addenda.porttrail.hostdemo.helper.RedisGeoHelper;
import io.lettuce.core.GeoCoordinates;
import io.lettuce.core.GeoWithin;
import io.lettuce.core.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisGeoService {

  @Autowired
  private RedisGeoHelper redisGeoHelper;

  public Long geoadd(String key, double longitude, double latitude, String member) {
    return redisGeoHelper.geoadd(key, longitude, latitude, member);
  }

  public Long geoaddMulti(String key, List<Double> longitudes, List<Double> latitudes, List<String> members) {
    return redisGeoHelper.geoaddMulti(key, longitudes, latitudes, members);
  }

  public List<GeoCoordinates> geopos(String key, String... members) {
    return redisGeoHelper.geopos(key, members);
  }

  public Double geodist(String key, String from, String to, String unit) {
    return redisGeoHelper.geodist(key, from, to, unit);
  }

  public List<Value<String>> geohash(String key, String... members) {
    return redisGeoHelper.geohash(key, members);
  }

  public List<GeoWithin<String>> georadius(String key, double longitude, double latitude, double radius,
                                           String unit, boolean withDist, boolean withCoord, boolean withHash,
                                           long count, String sort) {
    return redisGeoHelper.georadius(key, longitude, latitude, radius, unit, withDist, withCoord, withHash, count, sort);
  }

  public List<GeoWithin<String>> georadiusbymember(String key, String member, double radius,
                                                   String unit, boolean withDist, boolean withCoord, boolean withHash,
                                                   long count, String sort) {
    return redisGeoHelper.georadiusbymember(key, member, radius, unit, withDist, withCoord, withHash, count, sort);
  }

  public List<GeoWithin<String>> geosearch(String key, double longitude, double latitude, double radius,
                                           String unit, boolean withDist, boolean withCoord, boolean withHash,
                                           long count, String sort) {
    return redisGeoHelper.geosearch(key, longitude, latitude, radius, unit, withDist, withCoord, withHash, count, sort);
  }

  public List<GeoWithin<String>> geosearchByMember(String key, String member, double radius,
                                                   String unit, boolean withDist, boolean withCoord, boolean withHash,
                                                   long count, String sort) {
    return redisGeoHelper.geosearchByMember(key, member, radius, unit, withDist, withCoord, withHash, count, sort);
  }

  public List<GeoWithin<String>> geosearchByBox(String key, double longitude, double latitude,
                                                double width, double height, String unit,
                                                boolean withDist, boolean withCoord, boolean withHash,
                                                long count, String sort) {
    return redisGeoHelper.geosearchByBox(key, longitude, latitude, width, height, unit, withDist, withCoord, withHash, count, sort);
  }

  public Long georadiusstore(String key, String destination, double longitude, double latitude,
                             double radius, String unit, long count, String sort, boolean storeDist) {
    return redisGeoHelper.georadiusstore(key, destination, longitude, latitude, radius, unit, count, sort, storeDist);
  }

  public Long georadiusbymemberstore(String key, String destination, String member, double radius,
                                     String unit, long count, String sort, boolean storeDist) {
    return redisGeoHelper.georadiusbymemberstore(key, destination, member, radius, unit, count, sort, storeDist);
  }

  public Long geosearchstore(String destination, String key, double longitude, double latitude,
                             double radius, String unit, long count, String sort, boolean storeDist) {
    return redisGeoHelper.geosearchstore(destination, key, longitude, latitude, radius, unit, count, sort, storeDist);
  }

}
