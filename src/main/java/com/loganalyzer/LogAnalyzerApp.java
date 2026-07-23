package com.loganalyzer;

import io.javalin.Javalin;
import java.util.Map;

public class LogAnalyzerApp {
    
    private static final ClaudeApiClient claudeClient = new ClaudeApiClient();
    
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        
        // Health check
        app.get("/health", ctx -> {
            ctx.json(Map.of("status", "UP", "service", "ai-log-analyzer"));
        });
        
        // Analyze logs endpoint
        app.post("/analyze", ctx -> {
            try {
                String logs = ctx.formParam("logs");
                String analysisType = ctx.formParam("type");
                
                if (logs == null || logs.trim().isEmpty()) {
                    ctx.status(400).json(Map.of("error", "Logs parameter required"));
                    return;
                }
                
                System.out.println("Analyzing " + analysisType + " logs");
                String analysis = claudeClient.analyzeLogs(logs, analysisType);
                
                ctx.json(Map.of(
                    "success", true,
                    "analysis", analysis,
                    "logLength", logs.length(),
                    "analysisType", analysisType
                ));
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                ctx.status(500).json(Map.of("error", e.getMessage()));
            }
        });
        
        // Serve UI
        app.get("/", ctx -> ctx.html(getHtmlUI()));
        
        System.out.println("🚀 AI Log Analyzer running on http://localhost:8080");
    }
    
    private static String getHtmlUI() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>AI Log Analyzer</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        padding: 20px;
                    }
                    .container {
                        max-width: 1000px;
                        margin: 0 auto;
                    }
                    .header {
                        color: white;
                        text-align: center;
                        margin-bottom: 40px;
                    }
                    .header h1 {
                        font-size: 2.5rem;
                        margin-bottom: 10px;
                    }
                    .card {
                        background: white;
                        border-radius: 12px;
                        padding: 30px;
                        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                        margin-bottom: 20px;
                    }
                    .form-group {
                        margin-bottom: 20px;
                    }
                    label {
                        display: block;
                        font-weight: 600;
                        margin-bottom: 8px;
                        color: #333;
                    }
                    textarea, select {
                        width: 100%;
                        padding: 12px;
                        border: 1px solid #ddd;
                        border-radius: 8px;
                        font-family: 'Courier New', monospace;
                        font-size: 14px;
                    }
                    textarea:focus, select:focus {
                        outline: none;
                        border-color: #667eea;
                        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                    }
                    textarea {
                        min-height: 200px;
                    }
                    button {
                        width: 100%;
                        padding: 12px 24px;
                        border: none;
                        border-radius: 8px;
                        font-size: 1rem;
                        font-weight: 600;
                        cursor: pointer;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        transition: all 0.3s;
                    }
                    button:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
                    }
                    button:disabled {
                        opacity: 0.6;
                        cursor: not-allowed;
                    }
                    .analysis-result {
                        background: #f8f9fa;
                        border-left: 4px solid #667eea;
                        padding: 20px;
                        border-radius: 8px;
                        white-space: pre-wrap;
                        font-family: 'Courier New', monospace;
                        font-size: 14px;
                        line-height: 1.6;
                        color: #333;
                    }
                    .loading {
                        text-align: center;
                        padding: 20px;
                        color: #667eea;
                    }
                    .error {
                        background: #fcebeb;
                        border-left: 4px solid #e74c3c;
                        padding: 20px;
                        border-radius: 8px;
                        color: #c0392b;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔍 AI Log Analyzer</h1>
                        <p>Demo Version - Intelligent log analysis (Mock Mode)</p>
<div style="background: #fff3cd; padding: 10px; margin-bottom: 20px; border-radius: 8px; border-left: 4px solid #ffc107;">
    ℹ️ This is a demo using simulated AI responses. 
    Production version uses real Claude API.
</div>
                    </div>
                    
                    <div class="card">
                        <form id="analyzeForm">
                            <div class="form-group">
                                <label for="analysisType">Analysis Type</label>
                                <select id="analysisType" name="type" required>
                                    <option value="errors">Error Detection</option>
                                    <option value="performance">Performance Issues</option>
                                    <option value="security">Security Concerns</option>
                                    <option value="patterns">Pattern Recognition</option>
                                    <option value="general">General Analysis</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="logsInput">Paste Your Logs Here</label>
                                <textarea id="logsInput" name="logs" placeholder="Paste server logs..." required></textarea>
                            </div>
                            
                            <button type="submit">Analyze with AI</button>
                        </form>
                    </div>
                    
                    <div id="results"></div>
                </div>
                
                <script>
                    const form = document.getElementById('analyzeForm');
                    const resultsDiv = document.getElementById('results');
                    
                    form.addEventListener('submit', async (e) => {
                        e.preventDefault();
                        
                        const formData = new FormData(form);
                        const btn = form.querySelector('button');
                        
                        btn.disabled = true;
                        btn.textContent = 'Analyzing...';
                        
                        resultsDiv.innerHTML = '<div class="loading">🤖 Claude is analyzing your logs...</div>';
                        
                        try {
                            const response = await fetch('/analyze', {
                                method: 'POST',
                                body: formData
                            });
                            
                            const data = await response.json();
                            
                            if (response.ok && data.success) {
                                resultsDiv.innerHTML = `
                                    <div class="card">
                                        <div class="analysis-result">${escapeHtml(data.analysis)}</div>
                                    </div>
                                `;
                            } else {
                                resultsDiv.innerHTML = `<div class="card"><div class="error">❌ Error: ${data.error}</div></div>`;
                            }
                        } catch (error) {
                            resultsDiv.innerHTML = `<div class="card"><div class="error">❌ Error: ${error.message}</div></div>`;
                        } finally {
                            btn.disabled = false;
                            btn.textContent = 'Analyze with AI';
                        }
                    });
                    
                    function escapeHtml(text) {
                        const div = document.createElement('div');
                        div.textContent = text;
                        return div.innerHTML;
                    }
                </script>
            </body>
            </html>
            """;
    }
}