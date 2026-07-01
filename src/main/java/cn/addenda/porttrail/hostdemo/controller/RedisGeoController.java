package cn.addenda.porttrail.hostdemo.controller;

import cn.addenda.porttrail.hostdemo.service.RedisGeoService;
import io.lettuce.core.GeoCoordinates;
import io.lettuce.core.GeoWithin;
import io.lettuce.core.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("redisGeo")
public class RedisGeoController {

  @Autowired
  private RedisGeoService redisGeoService;

  private Map<String, Object> ok(Object... kvs) {
    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < kvs.length; i += 2) map.put(String.valueOf(kvs[i]), kvs[i + 1]);
    return map;
  }

  private Map<String, Object> toMap(GeoCoordinates coord) {
    if (coord == null) return null;
    Map<String, Object> m = new HashMap<>();
    m.put("longitude", coord.getX());
    m.put("latitude", coord.getY());
    return m;
  }

  private Map<String, Object> toMap(GeoWithin<String> gw) {
    if (gw == null) return null;
    Map<String, Object> m = new HashMap<>();
    m.put("member", gw.getMember());
    if (gw.getDistance() != null) m.put("distance", gw.getDistance());
    if (gw.getCoordinates() != null) {
      m.put("longitude", gw.getCoordinates().getX());
      m.put("latitude", gw.getCoordinates().getY());
    }
    if (gw.getGeohash() != null) m.put("geohash", gw.getGeohash());
    return m;
  }

  private List<Map<String, Object>> toList(List<GeoWithin<String>> list) {
    if (list == null) return Collections.emptyList();
    return list.stream().map(this::toMap).collect(Collectors.toList());
  }

  // ==================== GEOADD ====================

  /**
   * 添加一个地理位置到 Geo 集合
   * POST /redisGeo/geoadd?key=geo:stores&longitude=116.404&latitude=39.915&member=store:1
   */
  @PostMapping("geoadd")
  public Map<String, Object> geoadd(@RequestParam("key") String key,
                                    @RequestParam("longitude") double longitude,
                                    @RequestParam("latitude") double latitude,
                                    @RequestParam("member") String member) {
    Long result = redisGeoService.geoadd(key, longitude, latitude, member);
    return ok("key", key, "longitude", longitude, "latitude", latitude, "member", member, "added", result);
  }

  /**
   * 批量添加多个地理位置（三个列表按顺序一一对应）
   * POST /redisGeo/geoaddMulti?key=geo:stores&longitudes=116.404,116.403&latitudes=39.915,39.918&members=store:1,store:2
   */
  @PostMapping("geoaddMulti")
  public Map<String, Object> geoaddMulti(@RequestParam("key") String key,
                                         @RequestParam("longitudes") List<Double> longitudes,
                                         @RequestParam("latitudes") List<Double> latitudes,
                                         @RequestParam("members") List<String> members) {
    Long result = redisGeoService.geoaddMulti(key, longitudes, latitudes, members);
    return ok("key", key, "longitudes", longitudes, "latitudes", latitudes, "members", members, "added", result);
  }

  // ==================== GEOPOS ====================

  /**
   * 获取成员的经纬度坐标
   * POST /redisGeo/geopos?key=geo:stores&members=store:1&members=store:2
   */
  @PostMapping("geopos")
  public Map<String, Object> geopos(@RequestParam("key") String key,
                                    @RequestParam("members") List<String> members) {
    List<GeoCoordinates> positions = redisGeoService.geopos(key, members.toArray(new String[0]));
    List<Map<String, Object>> posList = new ArrayList<>();
    for (int i = 0; i < members.size(); i++) {
      Map<String, Object> m = new HashMap<>();
      m.put("member", members.get(i));
      m.put("position", toMap(positions != null && i < positions.size() ? positions.get(i) : null));
      posList.add(m);
    }
    return ok("key", key, "positions", posList);
  }

  // ==================== GEODIST ====================

  /**
   * 计算两个成员之间的距离（默认单位 m，可选 km/mi/ft）
   * POST /redisGeo/geodist?key=geo:stores&from=store:1&to=store:2&unit=km
   */
  @PostMapping("geodist")
  public Map<String, Object> geodist(@RequestParam("key") String key,
                                     @RequestParam("from") String from,
                                     @RequestParam("to") String to,
                                     @RequestParam(value = "unit", defaultValue = "m") String unit) {
    Double distance = redisGeoService.geodist(key, from, to, unit);
    return ok("key", key, "from", from, "to", to, "unit", unit, "distance", distance);
  }

  // ==================== GEOHASH ====================

  /**
   * 获取成员的 Geohash 编码
   * POST /redisGeo/geohash?key=geo:stores&members=store:1&members=store:2
   */
  @PostMapping("geohash")
  public Map<String, Object> geohash(@RequestParam("key") String key,
                                     @RequestParam("members") List<String> members) {
    List<Value<String>> hashes = redisGeoService.geohash(key, members.toArray(new String[0]));
    List<Map<String, Object>> hashList = new ArrayList<>();
    for (int i = 0; i < members.size(); i++) {
      Map<String, Object> m = new HashMap<>();
      m.put("member", members.get(i));
      m.put("geohash", hashes != null && i < hashes.size() ? hashes.get(i).getValue() : null);
      hashList.add(m);
    }
    return ok("key", key, "hashes", hashList);
  }

  // ==================== GEORADIUS ====================

  /**
   * 以经纬度为中心，搜索指定半径内成员（支持 WITHDIST/WITHCOORD/WITHHASH/COUNT/SORT）
   * POST /redisGeo/georadius?key=geo:stores&longitude=116.404&latitude=39.915&radius=5&unit=km&withDist=true&withCoord=true&withHash=false&count=10&sort=asc
   */
  @PostMapping("georadius")
  public Map<String, Object> georadius(@RequestParam("key") String key,
                                       @RequestParam("longitude") double longitude,
                                       @RequestParam("latitude") double latitude,
                                       @RequestParam("radius") double radius,
                                       @RequestParam(value = "unit", defaultValue = "m") String unit,
                                       @RequestParam(value = "withDist", defaultValue = "false") boolean withDist,
                                       @RequestParam(value = "withCoord", defaultValue = "false") boolean withCoord,
                                       @RequestParam(value = "withHash", defaultValue = "false") boolean withHash,
                                       @RequestParam(value = "count", defaultValue = "0") long count,
                                       @RequestParam(value = "sort", defaultValue = "asc") String sort) {
    List<GeoWithin<String>> result = redisGeoService.georadius(
            key, longitude, latitude, radius, unit, withDist, withCoord, withHash, count, sort);
    return ok("key", key, "longitude", longitude, "latitude", latitude, "radius", radius, "unit", unit,
            "count", result != null ? result.size() : 0, "members", toList(result));
  }

  // ==================== GEORADIUSBYMEMBER ====================

  /**
   * 以成员为中心，搜索指定半径内成员
   * POST /redisGeo/georadiusbymember?key=geo:stores&member=store:1&radius=5&unit=km&withDist=true&withCoord=true&withHash=false&count=10&sort=asc
   */
  @PostMapping("georadiusbymember")
  public Map<String, Object> georadiusbymember(@RequestParam("key") String key,
                                               @RequestParam("member") String member,
                                               @RequestParam("radius") double radius,
                                               @RequestParam(value = "unit", defaultValue = "m") String unit,
                                               @RequestParam(value = "withDist", defaultValue = "false") boolean withDist,
                                               @RequestParam(value = "withCoord", defaultValue = "false") boolean withCoord,
                                               @RequestParam(value = "withHash", defaultValue = "false") boolean withHash,
                                               @RequestParam(value = "count", defaultValue = "0") long count,
                                               @RequestParam(value = "sort", defaultValue = "asc") String sort) {
    List<GeoWithin<String>> result = redisGeoService.georadiusbymember(
            key, member, radius, unit, withDist, withCoord, withHash, count, sort);
    return ok("key", key, "member", member, "radius", radius, "unit", unit,
            "count", result != null ? result.size() : 0, "members", toList(result));
  }

  // ==================== GEOSEARCH (Redis 6.2+) ====================

  /**
   * 新版搜索：以经纬度为中心，搜索指定半径内成员（Redis 6.2+）
   * POST /redisGeo/geosearch?key=geo:stores&longitude=116.404&latitude=39.915&radius=5&unit=km&withDist=true&withCoord=true&withHash=false&count=10&sort=asc
   */
  @PostMapping("geosearch")
  public Map<String, Object> geosearch(@RequestParam("key") String key,
                                       @RequestParam("longitude") double longitude,
                                       @RequestParam("latitude") double latitude,
                                       @RequestParam("radius") double radius,
                                       @RequestParam(value = "unit", defaultValue = "m") String unit,
                                       @RequestParam(value = "withDist", defaultValue = "false") boolean withDist,
                                       @RequestParam(value = "withCoord", defaultValue = "false") boolean withCoord,
                                       @RequestParam(value = "withHash", defaultValue = "false") boolean withHash,
                                       @RequestParam(value = "count", defaultValue = "0") long count,
                                       @RequestParam(value = "sort", defaultValue = "asc") String sort) {
    List<GeoWithin<String>> result = redisGeoService.geosearch(
            key, longitude, latitude, radius, unit, withDist, withCoord, withHash, count, sort);
    return ok("key", key, "longitude", longitude, "latitude", latitude, "radius", radius, "unit", unit,
            "count", result != null ? result.size() : 0, "members", toList(result));
  }

  /**
   * 新版搜索：以成员为中心，搜索指定半径内成员（Redis 6.2+）
   * POST /redisGeo/geosearchByMember?key=geo:stores&member=store:1&radius=5&unit=km&withDist=true&withCoord=true&withHash=false&count=10&sort=asc
   */
  @PostMapping("geosearchByMember")
  public Map<String, Object> geosearchByMember(@RequestParam("key") String key,
                                               @RequestParam("member") String member,
                                               @RequestParam("radius") double radius,
                                               @RequestParam(value = "unit", defaultValue = "m") String unit,
                                               @RequestParam(value = "withDist", defaultValue = "false") boolean withDist,
                                               @RequestParam(value = "withCoord", defaultValue = "false") boolean withCoord,
                                               @RequestParam(value = "withHash", defaultValue = "false") boolean withHash,
                                               @RequestParam(value = "count", defaultValue = "0") long count,
                                               @RequestParam(value = "sort", defaultValue = "asc") String sort) {
    List<GeoWithin<String>> result = redisGeoService.geosearchByMember(
            key, member, radius, unit, withDist, withCoord, withHash, count, sort);
    return ok("key", key, "member", member, "radius", radius, "unit", unit,
            "count", result != null ? result.size() : 0, "members", toList(result));
  }

  /**
   * 新版搜索：在矩形区域内搜索成员（Redis 6.2+）
   * POST /redisGeo/geosearchByBox?key=geo:stores&longitude=116.3&latitude=39.8&width=10&height=10&unit=km&withDist=true&withCoord=true&withHash=false&count=20&sort=asc
   */
  @PostMapping("geosearchByBox")
  public Map<String, Object> geosearchByBox(@RequestParam("key") String key,
                                            @RequestParam("longitude") double longitude,
                                            @RequestParam("latitude") double latitude,
                                            @RequestParam("width") double width,
                                            @RequestParam("height") double height,
                                            @RequestParam(value = "unit", defaultValue = "m") String unit,
                                            @RequestParam(value = "withDist", defaultValue = "false") boolean withDist,
                                            @RequestParam(value = "withCoord", defaultValue = "false") boolean withCoord,
                                            @RequestParam(value = "withHash", defaultValue = "false") boolean withHash,
                                            @RequestParam(value = "count", defaultValue = "0") long count,
                                            @RequestParam(value = "sort", defaultValue = "asc") String sort) {
    List<GeoWithin<String>> result = redisGeoService.geosearchByBox(
            key, longitude, latitude, width, height, unit, withDist, withCoord, withHash, count, sort);
    return ok("key", key, "longitude", longitude, "latitude", latitude, "width", width, "height", height, "unit", unit,
            "count", result != null ? result.size() : 0, "members", toList(result));
  }

  // ==================== GEORADIUSSTORE ====================

  /**
   * 将 GEORADIUS 搜索结果存入另一个 key（被存储 key 即为 SortedSet）
   * POST /redisGeo/georadiusstore?key=geo:stores&destination=geo:nearby&longitude=116.404&latitude=39.915&radius=5&unit=km&count=10&sort=asc&storeDist=false
   */
  @PostMapping("georadiusstore")
  public Map<String, Object> georadiusstore(@RequestParam("key") String key,
                                            @RequestParam("destination") String destination,
                                            @RequestParam("longitude") double longitude,
                                            @RequestParam("latitude") double latitude,
                                            @RequestParam("radius") double radius,
                                            @RequestParam(value = "unit", defaultValue = "m") String unit,
                                            @RequestParam(value = "count", defaultValue = "0") long count,
                                            @RequestParam(value = "sort", defaultValue = "asc") String sort,
                                            @RequestParam(value = "storeDist", defaultValue = "false") boolean storeDist) {
    Long result = redisGeoService.georadiusstore(
            key, destination, longitude, latitude, radius, unit, count, sort, storeDist);
    return ok("key", key, "destination", destination, "longitude", longitude, "latitude", latitude,
            "radius", radius, "unit", unit, "count", count, "storeDist", storeDist, "stored", result);
  }

  // ==================== GEORADIUSBYMEMBERSTORE ====================

  /**
   * 将以成员为中心的搜索结果存入另一个 key
   * POST /redisGeo/georadiusbymemberstore?key=geo:stores&destination=geo:nearby&member=store:1&radius=5&unit=km&count=10&sort=asc&storeDist=true
   */
  @PostMapping("georadiusbymemberstore")
  public Map<String, Object> georadiusbymemberstore(@RequestParam("key") String key,
                                                    @RequestParam("destination") String destination,
                                                    @RequestParam("member") String member,
                                                    @RequestParam("radius") double radius,
                                                    @RequestParam(value = "unit", defaultValue = "m") String unit,
                                                    @RequestParam(value = "count", defaultValue = "0") long count,
                                                    @RequestParam(value = "sort", defaultValue = "asc") String sort,
                                                    @RequestParam(value = "storeDist", defaultValue = "false") boolean storeDist) {
    Long result = redisGeoService.georadiusbymemberstore(
            key, destination, member, radius, unit, count, sort, storeDist);
    return ok("key", key, "destination", destination, "member", member,
            "radius", radius, "unit", unit, "count", count, "storeDist", storeDist, "stored", result);
  }

  // ==================== GEOSEARCHSTORE ====================

  /**
   * 将 GEOSEARCH 的结果存入另一个 key（用于缓存搜索结果）
   * POST /redisGeo/geosearchstore?key=geo:stores&destination=geo:result&longitude=116.404&latitude=39.915&radius=5&unit=km&count=10&sort=asc&storeDist=false
   */
  @PostMapping("geosearchstore")
  public Map<String, Object> geosearchstore(@RequestParam("key") String key,
                                            @RequestParam("destination") String destination,
                                            @RequestParam("longitude") double longitude,
                                            @RequestParam("latitude") double latitude,
                                            @RequestParam("radius") double radius,
                                            @RequestParam(value = "unit", defaultValue = "m") String unit,
                                            @RequestParam(value = "count", defaultValue = "0") long count,
                                            @RequestParam(value = "sort", defaultValue = "asc") String sort,
                                            @RequestParam(value = "storeDist", defaultValue = "false") boolean storeDist) {
    Long result = redisGeoService.geosearchstore(
            destination, key, longitude, latitude, radius, unit, count, sort, storeDist);
    return ok("key", key, "destination", destination, "longitude", longitude, "latitude", latitude,
            "radius", radius, "unit", unit, "count", count, "storeDist", storeDist, "stored", result);
  }

}
