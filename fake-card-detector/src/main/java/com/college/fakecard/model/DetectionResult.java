package com.college.fakecard.model;

import java.util.List;

public record DetectionResult(String verdict, int riskScore, String confidence, int width, int height,
                              List<String> alerts, List<String> checks, String note) { }
