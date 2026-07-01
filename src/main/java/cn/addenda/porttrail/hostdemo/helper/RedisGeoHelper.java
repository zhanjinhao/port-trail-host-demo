package cn.addenda.porttrail.hostdemo.helper;

import io.lettuce.core.*;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RedisGeoHelper {

  @Resource
  private StatefulRedisClusterConnection<String, String> clusterConnection;

  private String tag(String key) {
    if (key == null || key.isEmpty()) return key;
    if (key.contains("{")) return key;
    int idx = key.indexOf(':');
    if (idx > 0) return "{" + key.substring(0, idx) + "}" + key.substring(idx);
    return "{" + key + "}";
  }

  private String[] tag(String... keys) {
    String[] result = new String[keys.length];
    for (int i = 0; i < keys.length; i++) result[i] = tag(keys[i]);
    return result;
  }

  private GeoArgs.Unit parseUnit(String unit) {
    if (unit == null) return GeoArgs.Unit.m;
    switch (unit.toLowerCase()) {
      case "km": return GeoArgs.Unit.km;
      case "mi": return GeoArgs.Unit.mi;
      case "ft": return GeoArgs.Unit.ft;
      default: return GeoArgs.Unit.m;
    }
  }

  private GeoArgs buildGeoArgs(boolean withDist, boolean withCoord, boolean withHash, long count, String sort) {
    GeoArgs args = new GeoArgs();
    if (withDist) args = args.withDistance();
    if (withCoord) args = args.withCoordinates();
    if (withHash) args = args.withHash();
    if (count > 0) args = args.withCount(count);
    if ("desc".equalsIgnoreCase(sort)) {
      args = args.sort(GeoArgs.Sort.desc);
    } else if ("asc".equalsIgnoreCase(sort)) {
      args = args.sort(GeoArgs.Sort.asc);
    }
    return args;
  }

  // ==================== GEOADD ====================

  public Long geoadd(String key, double longitude, double latitude, String member) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      Long result = sync.geoadd(key, longitude, latitude, member);
      log.info("GEOADD key={}, lng={}, lat={}, member={}, result={}", key, longitude, latitude, member, result);
      return result;
    } catch (Exception e) {
      log.error("GEOADD command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOADD operation failed", e);
    }
  }

  public Long geoaddMulti(String key, List<Double> longitudes, List<Double> latitudes, List<String> members) {
    key = tag(key);
    try {
      if (longitudes.size() != latitudes.size() || longitudes.size() != members.size()) {
        throw new IllegalArgumentException("longitudes/latitudes/members size mismatch");
      }
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<Object> args = new ArrayList<>();
      for (int i = 0; i < longitudes.size(); i++) {
        args.add(longitudes.get(i));
        args.add(latitudes.get(i));
        args.add(members.get(i));
      }
      Long result = sync.geoadd(key, args.toArray());
      log.info("GEOADD_MULTI key={}, count={}, result={}", key, longitudes.size(), result);
      return result;
    } catch (Exception e) {
      log.error("GEOADD_MULTI command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOADD_MULTI operation failed", e);
    }
  }

  // ==================== GEOPOS ====================

  public List<GeoCoordinates> geopos(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<GeoCoordinates> positions = sync.geopos(key, members);
      log.info("GEOPOS key={}, members={}, size={}", key, String.join(",", members), positions != null ? positions.size() : 0);
      return positions;
    } catch (Exception e) {
      log.error("GEOPOS command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOPOS operation failed", e);
    }
  }

  // ==================== GEODIST ====================

  public Double geodist(String key, String from, String to, String unit) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoArgs.Unit geoUnit = parseUnit(unit);
      Double distance = sync.geodist(key, from, to, geoUnit);
      log.info("GEODIST key={}, from={}, to={}, unit={}, distance={}", key, from, to, unit, distance);
      return distance;
    } catch (Exception e) {
      log.error("GEODIST command failed, key={}", key, e);
      throw new RuntimeException("Redis GEODIST operation failed", e);
    }
  }

  // ==================== GEOHASH ====================

  public List<Value<String>> geohash(String key, String... members) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      List<Value<String>> result = sync.geohash(key, members);
      log.info("GEOHASH key={}, members={}, size={}", key, String.join(",", members), result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("GEOHASH command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOHASH operation failed", e);
    }
  }

  // ==================== GEORADIUS ====================

  public List<GeoWithin<String>> georadius(String key, double longitude, double latitude, double radius,
                                           String unit, boolean withDist, boolean withCoord, boolean withHash,
                                           long count, String sort) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoArgs args = buildGeoArgs(withDist, withCoord, withHash, count, sort);
      GeoArgs.Unit geoUnit = parseUnit(unit);
      List<GeoWithin<String>> result = sync.georadius(key, longitude, latitude, radius, geoUnit, args);
      log.info("GEORADIUS key={}, lng={}, lat={}, radius={}, unit={}, size={}",
              key, longitude, latitude, radius, unit, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("GEORADIUS command failed, key={}", key, e);
      throw new RuntimeException("Redis GEORADIUS operation failed", e);
    }
  }

  // ==================== GEORADIUSBYMEMBER ====================

  public List<GeoWithin<String>> georadiusbymember(String key, String member, double radius,
                                                   String unit, boolean withDist, boolean withCoord, boolean withHash,
                                                   long count, String sort) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoArgs args = buildGeoArgs(withDist, withCoord, withHash, count, sort);
      GeoArgs.Unit geoUnit = parseUnit(unit);
      List<GeoWithin<String>> result = sync.georadiusbymember(key, member, radius, geoUnit, args);
      log.info("GEORADIUSBYMEMBER key={}, member={}, radius={}, unit={}, size={}",
              key, member, radius, unit, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("GEORADIUSBYMEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis GEORADIUSBYMEMBER operation failed", e);
    }
  }

  // ==================== GEOSEARCH (Redis 6.2+) ====================

  public List<GeoWithin<String>> geosearch(String key, double longitude, double latitude, double radius,
                                           String unit, boolean withDist, boolean withCoord, boolean withHash,
                                           long count, String sort) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoSearch.GeoRef<String> ref = GeoSearch.fromCoordinates(longitude, latitude);
      GeoSearch.GeoPredicate predicate = GeoSearch.byRadius(radius, parseUnit(unit));
      GeoArgs args = buildGeoArgs(withDist, withCoord, withHash, count, sort);
      List<GeoWithin<String>> result = sync.geosearch(key, ref, predicate, args);
      log.info("GEOSEARCH key={}, lng={}, lat={}, radius={}, unit={}, size={}",
              key, longitude, latitude, radius, unit, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("GEOSEARCH command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOSEARCH operation failed", e);
    }
  }

  public List<GeoWithin<String>> geosearchByMember(String key, String member, double radius,
                                                   String unit, boolean withDist, boolean withCoord, boolean withHash,
                                                   long count, String sort) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoSearch.GeoRef<String> ref = GeoSearch.fromMember(member);
      GeoSearch.GeoPredicate predicate = GeoSearch.byRadius(radius, parseUnit(unit));
      GeoArgs args = buildGeoArgs(withDist, withCoord, withHash, count, sort);
      List<GeoWithin<String>> result = sync.geosearch(key, ref, predicate, args);
      log.info("GEOSEARCH_BY_MEMBER key={}, member={}, radius={}, unit={}, size={}",
              key, member, radius, unit, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("GEOSEARCH_BY_MEMBER command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOSEARCH_BY_MEMBER operation failed", e);
    }
  }

  public List<GeoWithin<String>> geosearchByBox(String key, double longitude, double latitude,
                                                double width, double height, String unit,
                                                boolean withDist, boolean withCoord, boolean withHash,
                                                long count, String sort) {
    key = tag(key);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoSearch.GeoRef<String> ref = GeoSearch.fromCoordinates(longitude, latitude);
      GeoSearch.GeoPredicate predicate = GeoSearch.byBox(width, height, parseUnit(unit));
      GeoArgs args = buildGeoArgs(withDist, withCoord, withHash, count, sort);
      List<GeoWithin<String>> result = sync.geosearch(key, ref, predicate, args);
      log.info("GEOSEARCH_BY_BOX key={}, lng={}, lat={}, width={}, height={}, unit={}, size={}",
              key, longitude, latitude, width, height, unit, result != null ? result.size() : 0);
      return result;
    } catch (Exception e) {
      log.error("GEOSEARCH_BY_BOX command failed, key={}", key, e);
      throw new RuntimeException("Redis GEOSEARCH_BY_BOX operation failed", e);
    }
  }

  // ==================== GEORADIUSSTORE ====================

  public Long georadiusstore(String key, String destination, double longitude, double latitude,
                             double radius, String unit, long count, String sort, boolean storeDist) {
    key = tag(key);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoArgs.Unit geoUnit = parseUnit(unit);
      GeoRadiusStoreArgs<String> storeArgs = new GeoRadiusStoreArgs<>();
      if (storeDist) {
        storeArgs = storeArgs.withStoreDist(destination);
      } else {
        storeArgs = storeArgs.withStore(destination);
      }
      if (count > 0) storeArgs = storeArgs.withCount(count);
      if ("desc".equalsIgnoreCase(sort)) storeArgs = storeArgs.sort(GeoArgs.Sort.desc);
      else if ("asc".equalsIgnoreCase(sort)) storeArgs = storeArgs.sort(GeoArgs.Sort.asc);
      Long result = sync.georadius(key, longitude, latitude, radius, geoUnit, storeArgs);
      log.info("GEORADIUSSTORE destination={}, key={}, lng={}, lat={}, radius={}, count={}, storeDist={}, result={}",
              destination, key, longitude, latitude, radius, count, storeDist, result);
      return result;
    } catch (Exception e) {
      log.error("GEORADIUSSTORE command failed, key={}", key, e);
      throw new RuntimeException("Redis GEORADIUSSTORE operation failed", e);
    }
  }

  // ==================== GEORADIUSBYMEMBERSTORE ====================

  public Long georadiusbymemberstore(String key, String destination, String member, double radius,
                                     String unit, long count, String sort, boolean storeDist) {
    key = tag(key);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoArgs.Unit geoUnit = parseUnit(unit);
      GeoRadiusStoreArgs<String> storeArgs = new GeoRadiusStoreArgs<>();
      if (storeDist) {
        storeArgs = storeArgs.withStoreDist(destination);
      } else {
        storeArgs = storeArgs.withStore(destination);
      }
      if (count > 0) storeArgs = storeArgs.withCount(count);
      if ("desc".equalsIgnoreCase(sort)) storeArgs = storeArgs.sort(GeoArgs.Sort.desc);
      else if ("asc".equalsIgnoreCase(sort)) storeArgs = storeArgs.sort(GeoArgs.Sort.asc);
      Long result = sync.georadiusbymember(key, member, radius, geoUnit, storeArgs);
      log.info("GEORADIUSBYMEMBERSTORE destination={}, key={}, member={}, radius={}, count={}, storeDist={}, result={}",
              destination, key, member, radius, count, storeDist, result);
      return result;
    } catch (Exception e) {
      log.error("GEORADIUSBYMEMBERSTORE command failed, key={}", key, e);
      throw new RuntimeException("Redis GEORADIUSBYMEMBERSTORE operation failed", e);
    }
  }

  // ==================== GEOSEARCHSTORE (Redis 6.2+) ====================

  public Long geosearchstore(String destination, String key, double longitude, double latitude,
                             double radius, String unit, long count, String sort, boolean storeDist) {
    key = tag(key);
    destination = tag(destination);
    try {
      RedisClusterCommands<String, String> sync = clusterConnection.sync();
      GeoSearch.GeoRef<String> ref = GeoSearch.fromCoordinates(longitude, latitude);
      GeoSearch.GeoPredicate predicate = GeoSearch.byRadius(radius, parseUnit(unit));
      GeoArgs args = buildGeoArgs(false, false, false, count, sort);
      Long result = sync.geosearchstore(destination, key, ref, predicate, args, storeDist);
      log.info("GEOSEARCHSTORE destination={}, key={}, lng={}, lat={}, radius={}, count={}, storeDist={}, result={}",
              destination, key, longitude, latitude, radius, count, storeDist, result);
      return result;
    } catch (Exception e) {
      log.error("GEOSEARCHSTORE command failed, destination={}, key={}", destination, key, e);
      throw new RuntimeException("Redis GEOSEARCHSTORE operation failed", e);
    }
  }

}
