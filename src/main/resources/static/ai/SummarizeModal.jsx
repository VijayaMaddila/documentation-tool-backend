import { useState, useEffect } from "react";

// Props:
//   content — current document HTML/text content from Quill
//   onClose() — closes the modal
export default function SummarizeModal({ content, onClose }) {
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await fetch("http://localhost:8080/api/ai/summarize", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ content }),
        });
        if (!res.ok) throw new Error("Failed to summarize document");
        const data = await res.json();
        setSummary(data.summary);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchSummary();
  }, [content]);

  return (
    <div style={styles.overlay}>
      <div style={styles.modal}>
        <h2 style={styles.title}>Document Summary</h2>
        {loading && <p style={styles.loading}>Summarizing...</p>}
        {error && <p style={styles.error}>{error}</p>}
        {summary && <p style={styles.summary}>{summary}</p>}
        <div style={styles.actions}>
          <button style={styles.closeBtn} onClick={onClose}>
            Close
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
    background: "#fff", borderRadius: 8, padding: 24, width: 520,
    boxShadow: "0 4px 20px rgba(0,0,0,0.2)", display: "flex", flexDirection: "column", gap: 12,
    maxHeight: "80vh", overflowY: "auto",
  },
  title: { margin: 0, fontSize: 18, fontWeight: 600 },
  loading: { color: "#718096", fontSize: 14 },
  error: { color: "#e53e3e", fontSize: 13, margin: 0 },
  summary: { fontSize: 14, lineHeight: 1.7, color: "#2d3748", whiteSpace: "pre-wrap" },
  actions: { display: "flex", justifyContent: "flex-end" },
  closeBtn: {
    padding: "8px 16px", borderRadius: 6, border: "none",
    background: "#4f46e5", color: "#fff", cursor: "pointer", fontSize: 14,
  },
};
