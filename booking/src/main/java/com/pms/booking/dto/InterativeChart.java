package com.pms.booking.dto;

import java.util.List;

public record InterativeChart(List<String> labels, String label, List<Double> data) {
}
