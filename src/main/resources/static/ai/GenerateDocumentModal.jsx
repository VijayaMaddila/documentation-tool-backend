import { useState } from "react";

// Props:
//   onInsert(htmlContent) — called with AI-generated content to insert into Quill
//   onClose() — closes the modal
export default function GenerateDocumentModal({ onInsert, onClose }) {
  const [prompt, setPrompt] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleGenerate = async () => {
    if (!prompt.trim()) return;
    setLoading(true);
    setError("");
    try {
      const token = localStorage.getItem("token");
      const res = await fetch("http://localhost:8080/api/ai/generate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ prompt }),
      });
      if (!res.ok) throw new Error("Failed to generate content");
      const data = await res.json();
      onInsert(data.content);
      onClose();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.overlay}>
      <div style={styles.modal}>
        <h2 style={styles.title}>Generate Document</h2>
        <textarea
          style={styles.textarea}
          placeholder="Describe what you want to write..."
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          rows={5}
          disabled={loading}
        />
        {error && <p style={styles.error}>{error}</p>}
        <div style={styles.actions}>
          <button style={styles.cancelBtn} onClick={onClose} disabled={loading}>
            Cancel
          </button>
          <button style={styles.generateBtn} onClick={handleGenerate} disabled={loading || !prompt.trim()}>
            {loading ? "Generating..." : "Generate"}
          </button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  overlay: {
    position: "fixed", inset: 0, background: "rgba(0,0,0,0.5)",
    display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000,
  },
  modal: {
    background: "#fff", borderRadius: 8, padding: 24, width: 480,
    boxShadow: "0 4px 20px rgba(0,0,0,0.2)", display: "flex", flexDirection: "column", gap: 12,
  },
  title: { margin: 0, fontSize: 18, fontWeight: 600 },
  textarea: {
    width: "100%", padding: 10, borderRadius: 6, border: "1px solid #ddd",
    fontSize: 14, resize: "vertical", boxSizing: "border-box",
  },
  error: { color: "#e53e3e", fontSize: 13, margin: 0 },
  actions: { display: "flex", justifyContent: "flex-end", gap: 8 },
  cancelBtn: {
    padding: "8px 16px", borderRadius: 6, border: "1px solid #ddd",
    background: "#fff", cursor: "pointer", fontSize: 14,
  },
  generateBtn: {
    padding: "8px 16px", borderRadius: 6, border: "none",
    background: "#4f46e5", color: "#fff", cursor: "pointer", fontSize: 14,
  },
};
