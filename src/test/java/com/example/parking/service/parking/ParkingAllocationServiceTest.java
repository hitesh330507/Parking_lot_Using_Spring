package com.example.parking.service.parking;

import com.example.parking.domain.enums.SlotStatus;
import com.example.parking.domain.enums.VehicleType;
import com.example.parking.domain.model.Coordinate;
import com.example.parking.domain.model.Floor;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.domain.model.ParkingSlot;
import com.example.parking.domain.model.ParkingZone;
import com.example.parking.exception.NoAvailableSlotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParkingAllocationServiceTest {

    private ParkingAllocationService allocationService;

    @BeforeEach
    void setUp() throws Exception {
        allocationService = new ParkingAllocationService();
        Field field = ParkingAllocationService.class.getDeclaredField("activeVehicles");
        field.setAccessible(true);
        field.set(allocationService, new NullFriendlyConcurrentMap<>());
    }

    private static class NullFriendlyConcurrentMap<K, V> implements ConcurrentMap<K, V> {
        private final Map<K, V> delegate = new HashMap<>();

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return delegate.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return delegate.containsValue(value);
        }

        @Override
        public V get(Object key) {
            return delegate.get(key);
        }

        @Override
        public V put(K key, V value) {
            return delegate.put(key, value);
        }

        @Override
        public V remove(Object key) {
            return delegate.remove(key);
        }

        @Override
        public void putAll(java.util.Map<? extends K, ? extends V> m) {
            delegate.putAll(m);
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public java.util.Set<K> keySet() {
            return delegate.keySet();
        }

        @Override
        public java.util.Collection<V> values() {
            return delegate.values();
        }

        @Override
        public java.util.Set<Entry<K, V>> entrySet() {
            return delegate.entrySet();
        }

        @Override
        public V getOrDefault(Object key, V defaultValue) {
            return delegate.getOrDefault(key, defaultValue);
        }

        @Override
        public void forEach(java.util.function.BiConsumer<? super K, ? super V> action) {
            delegate.forEach(action);
        }

        @Override
        public void replaceAll(java.util.function.BiFunction<? super K, ? super V, ? extends V> function) {
            delegate.replaceAll(function);
        }

        @Override
        public V putIfAbsent(K key, V value) {
            return delegate.putIfAbsent(key, value);
        }

        @Override
        public boolean remove(Object key, Object value) {
            return delegate.remove(key, value);
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            return delegate.replace(key, oldValue, newValue);
        }

        @Override
        public V replace(K key, V value) {
            return delegate.replace(key, value);
        }

        @Override
        public V computeIfAbsent(K key, java.util.function.Function<? super K, ? extends V> mappingFunction) {
            return delegate.computeIfAbsent(key, mappingFunction);
        }

        @Override
        public V computeIfPresent(K key, java.util.function.BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            return delegate.computeIfPresent(key, remappingFunction);
        }

        @Override
        public V compute(K key, java.util.function.BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            return delegate.compute(key, remappingFunction);
        }

        @Override
        public V merge(K key, V value, java.util.function.BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            return delegate.merge(key, value, remappingFunction);
        }
    }

    @Test
    void allocateSlot_shouldOccupyFirstAvailableCompatibleSlot() {
        ParkingSite site = createSiteWithSlot(VehicleType.CAR, SlotStatus.AVAILABLE);

        allocationService.allocateSlot(site, "ENTRY-1", "KA01AB1234", VehicleType.CAR);

        ParkingSlot slot = site.getFloors().get(0).getZones().get(0).getSlots().get(0);
        assertThat(slot.getStatus()).isEqualTo(SlotStatus.OCCUPIED);
    }

    @Test
    void allocateSlot_shouldThrowNoAvailableSlotException_whenNoSlotAvailable() {
        ParkingSite site = createSiteWithSlot(VehicleType.BIKE, SlotStatus.AVAILABLE);

        assertThatThrownBy(() -> allocationService.allocateSlot(site, "ENTRY-1", "KA01AB1234", VehicleType.CAR))
                .isInstanceOf(NoAvailableSlotException.class)
                .hasMessageContaining("No compatible slot available");
    }

    @Test
    void allocateSlot_shouldThrowNoAvailableSlotException_whenVehicleAlreadyParked() {
        ParkingSite site = createSiteWithSlot(VehicleType.CAR, SlotStatus.AVAILABLE);

        allocationService.allocateSlot(site, "ENTRY-1", "KA01AB1234", VehicleType.CAR);

        assertThatThrownBy(() -> allocationService.allocateSlot(site, "ENTRY-1", "KA01AB1234", VehicleType.CAR))
                .isInstanceOf(NoAvailableSlotException.class)
                .hasMessageContaining("Vehicle is already parked");
    }

    private ParkingSite createSiteWithSlot(VehicleType type, SlotStatus status) {
        ParkingSite site = new ParkingSite("SITE-1", "Test Site", null, Map.of(), 10, 1, 1, 20, 20);
        Floor floor = new Floor("SITE-1-F1", 1, 20, 20);
        ParkingZone zone = new ParkingZone("SITE-1-F1-Z1", 1, new Coordinate(0, 0), new Coordinate(10, 10));
        ParkingSlot slot = new ParkingSlot("SITE-1-F1-S1", 1, floor.getFloorId(), zone.getZoneId(), type, new Coordinate(1, 1));
        if (status == SlotStatus.OCCUPIED) {
            slot.occupy();
        }
        zone.addSlot(slot);
        floor.addZone(zone);
        site.addFloor(floor);
        return site;
    }
}
