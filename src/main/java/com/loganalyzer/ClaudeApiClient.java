package com.loganalyzer;

public class ClaudeApiClient {
    
    private static final String API_KEY = System.getenv("ANTHROPIC_API_KEY");
    private static final boolean USE_MOCK = true;  // Force mock mode for testing

    public String analyzeLogs(String logs, String analysisType) {
        System.out.println("Using " + (USE_MOCK ? "MOCK" : "REAL") + " mode");
        
        if (USE_MOCK) {
            return generateMockAnalysis(logs, analysisType);
        }
        
        // Real Claude API would go here (when you add credits)
        return "Real Claude API not available. Add API key and credits to enable.";
    }
    
    private String generateMockAnalysis(String logs, String analysisType) {
        return switch(analysisType) {
            case "errors" -> generateErrorAnalysis(logs);
            case "performance" -> generatePerformanceAnalysis(logs);
            case "security" -> generateSecurityAnalysis(logs);
            case "patterns" -> generatePatternAnalysis(logs);
            default -> generateGeneralAnalysis(logs);
        };
    }
    
    private String generateErrorAnalysis(String logs) {
        int errorCount = countOccurrences(logs, "ERROR");
        int warnCount = countOccurrences(logs, "WARN");
        
        return """
            === ERROR ANALYSIS REPORT ===
            
            Summary:
            • Total Errors Found: %d
            • Warnings Found: %d
            • Severity: %s
            
            Key Issues Identified:
            1. Kafka Broker Connection Issues
               - Multiple timeout errors detected
               - Connection pool exhaustion (100/100 connections)
               - Retry logic triggering repeatedly
            
            2. Message Queue Backlog
               - 5000+ messages pending
               - Consumer lag increasing
               - Offset synchronization problems
            
            3. System Resource Pressure
               - Memory usage at 85%% of heap
               - OutOfMemoryError risk
               - Connection pool saturation
            
            Root Causes:
            • Broker is overloaded or unresponsive
            • Network connectivity issues between services
            • Insufficient heap memory allocation
            • Consumer processing slower than message arrival
            
            Recommendations:
            1. Increase JVM heap size (-Xmx flag)
            2. Scale up consumer instances
            3. Check Kafka broker health and metrics
            4. Implement circuit breaker pattern
            5. Add monitoring for connection pool
            6. Implement backpressure handling
            
            Action Priority: HIGH - Address immediately
            """.formatted(errorCount, warnCount, errorCount > 5 ? "CRITICAL" : "HIGH");
    }
    
    private String generatePerformanceAnalysis(String logs) {
        return """
            === PERFORMANCE ANALYSIS REPORT ===
            
            Performance Metrics:
            • Slow Query Detected: 3.2 seconds (threshold: 1s)
            • Query Slowdown Factor: 3.2x
            • Connection Pool Status: EXHAUSTED (100/100)
            
            Bottlenecks Identified:
            1. Database Query Performance
               - Query execution time: 3.2s (should be <1s)
               - N+1 query problem likely
               - Missing database indexes
            
            2. Connection Pool Saturation
               - All 100 connections in use
               - Connection wait queue building up
               - Cascading slowdown to dependent services
            
            3. Memory Pressure
               - Heap usage: 85%% (critical)
               - Garbage collection pauses likely
               - Memory leak potential
            
            4. Throughput Degradation
               - Query backlog accumulating
               - Response times increasing exponentially
               - User experience impacted
            
            Solutions:
            1. Optimize database queries:
               - Add missing indexes
               - Implement query caching
               - Use prepared statements
            
            2. Increase connection pool:
               - Increase max connections to 150-200
               - Implement connection validation
               - Add connection timeout handling
            
            3. Memory optimization:
               - Increase JVM heap (-Xmx2g)
               - Enable GC logging
               - Profile application for leaks
            
            4. Caching strategy:
               - Implement Redis/Memcached
               - Add query result caching
               - Cache frequently accessed data
            
            Priority: CRITICAL - Affects user experience
            """;
    }
    
    private String generateSecurityAnalysis(String logs) {
        return """
            === SECURITY THREAT ANALYSIS ===
            
            Threat Level: CRITICAL ⚠️
            
            Detected Threats:
            1. Brute Force Attack
               • IP: 192.168.1.100
               • Failed attempts: 3+ in 15 seconds
               • Target: Admin account
               • Status: ACCOUNT LOCKED
            
            2. SQL Injection Attempt
               • Payload: DROP TABLE users
               • Attack vector: SQL query parameter
               • Detection: Pattern match successful
               • Status: BLOCKED
            
            3. Unauthorized Access
               • Target endpoint: /api/admin
               • Source IP: 192.168.1.100 (same as brute force)
               • Response: 403 Forbidden
               • Status: BLOCKED
            
            Attack Profile:
            • Single attacker IP attempting multiple vectors
            • Coordinated attack pattern
            • Targeting administrative functions
            • Time window: < 30 seconds
            
            Immediate Actions Required:
            1. BLOCK IP: 192.168.1.100
               - Add to firewall blacklist
               - Implement rate limiting
            
            2. AUDIT LOGS:
               - Check for successful breaches
               - Verify data integrity
               - Review access logs
            
            3. INCIDENT RESPONSE:
               - Alert security team
               - Enable enhanced monitoring
               - Implement 2FA for admin accounts
            
            4. HARDENING:
               - Update WAF rules
               - Implement CAPTCHA
               - Add request signing
            
            Compliance: GDPR/HIPAA incident logging enabled
            """;
    }
    
    private String generatePatternAnalysis(String logs) {
        return """
            === PATTERN & TREND ANALYSIS ===
            
            Identified Patterns:
            1. CASCADING FAILURE PATTERN
               • Connection pool exhaustion → Timeouts → Errors
               • Time window: 6 seconds
               • Root trigger: High load spike at 10:45:12
            
            2. EXPONENTIAL ERROR GROWTH
               • t=0s: 1 error
               • t=3s: 2 errors
               • t=6s: 4+ errors
               • Pattern: Doubles every 3 seconds
            
            3. RESOURCE DEPLETION SEQUENCE
               • Step 1: Query latency increases (3.2s)
               • Step 2: Connections exhaust (100/100)
               • Step 3: New requests queue
               • Step 4: Memory pressure builds (85%)
               • Step 5: OutOfMemoryError triggers
            
            Temporal Correlations:
            • 10:45:12 - First slow query logged
            • 10:45:15 - Connection pool warning
            • 10:45:18 - Memory spike detected
            • 10:45:20 - System failure cascade
            
            Predictive Insights:
            ⚠️ If patterns continue:
            • System will fail completely within 2-3 minutes
            • Data loss risk if not mitigated
            • Recovery time: 15-30 minutes
            
            Similar Historical Patterns:
            • This pattern matches incident from Jan 10 (resolved by scaling)
            • Pattern also seen in load testing (expected)
            • First time in production under this load
            
            Recommendations:
            1. Implement auto-scaling (add instances at 70% load)
            2. Set circuit breakers (fail fast at 80% pool usage)
            3. Enable bulkhead isolation (separate thread pools)
            4. Add synthetic load testing to CI/CD
            """;
    }
    
    private String generateGeneralAnalysis(String logs) {
        int errorCount = countOccurrences(logs, "ERROR");
        int warnCount = countOccurrences(logs, "WARN");
        int infoCount = countOccurrences(logs, "INFO");
        
        return """
            === COMPREHENSIVE LOG ANALYSIS ===
            
            Log Statistics:
            • Total Error entries: %d
            • Total Warning entries: %d
            • Total Info entries: %d
            • Analysis timestamp: %s
            
            Overall Health: DEGRADED ⚠️
            
            System Status:
            1. Connectivity Issues
               ✗ Kafka broker timeouts
               ✗ Connection pool exhausted
               ✗ Failed partition fetches
            
            2. Resource Constraints
               ✗ Memory usage: 85%% (critical)
               ✗ Connection pool: 100/100 (full)
               ✗ Heap space warnings
            
            3. Data Pipeline Health
               ⚠ Consumer lag increasing
               ⚠ Message queue backlog: 5000+
               ⚠ Processing delays detected
            
            Key Findings:
            • System is under heavy load
            • Resource exhaustion imminent
            • Cascading failures likely if not addressed
            • Multiple services affected
            
            Immediate Recommendations:
            1. Scale up infrastructure
            2. Increase resource limits
            3. Implement throttling/backpressure
            4. Monitor closely for further degradation
            5. Prepare rollback procedures
            
            Investigation Suggested For:
            • Query optimization
            • Connection pooling configuration
            • Memory leak analysis
            • Load distribution
            
            Follow-up Actions:
            1. Conduct performance review
            2. Load test infrastructure
            3. Implement auto-scaling
            4. Add alerting for these patterns
            """.formatted(errorCount, warnCount, infoCount, getCurrentTime());
    }
    
    private int countOccurrences(String text, String pattern) {
        return (int) text.split(pattern, -1).length - 1;
    }
    
    private String getCurrentTime() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
}