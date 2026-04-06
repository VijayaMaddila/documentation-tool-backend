// Example: how to integrate AI features into your existing Quill editor component
// Copy the relevant parts into your actual editor component

import { useState, useRef } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import GenerateDocumentModal from "./GenerateDocumentModal";
import SummarizeModal from "./SummarizeModal";

export default function DocumentEditor() {
  const [content, setContent] = useState("");
  const [showGenerate, setShowGenerate] = useState(false);
  const [showSummarize, setShowSummarize] = useState(false);
  const quillRef = useRef(null);

  // Insert AI-generated HTML at current cursor position (or end)
  const handleInsertContent = (htmlContent) => {
    const quill = quillRef.current?.getEditor();
    if (!quill) return;
    const range = quill.getSelection(true);
    const index = range ? range.index : quill.getLength();
    quill.clipboard.dangerouslyPasteHTML(index, htmlContent);
  };

  return (
    <div>
      {/* AI Toolbar Buttons — add these near your existing toolbar */}
      <div style={{ display: "flex", gap: 8, marginBottom: 8 }}>
        <button onClick={() => setShowGenerate(true)}>✨ Generate</button>
        <button onClick={() => setShowSummarize(true)} disabled={!content.trim()}>
          📄 Summarize
        </button>
      </div>

      <ReactQuill
        ref={quillRef}
        value={content}
        onChange={setContent}
        theme="snow"
      />

      {showGenerate && (
        <GenerateDocumentModal
          onInsert={handleInsertContent}
          onClose={() => setShowGenerate(false)}
        />
      )}

      {showSummarize && (
        <SummarizeModal
          content={content}
          onClose={() => setShowSummarize(false)}
        />
      )}
    </div>
  );
}
