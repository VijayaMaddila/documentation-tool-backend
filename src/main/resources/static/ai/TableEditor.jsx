import { useState } from "react";

const API = "http://localhost:8080/api/ai";

const emptyTable = () => [
  ["Header 1", "Header 2", "Header 3"],
  ["", "", ""],
  ["", "", ""],
];

export default function TableEditor({ token }) {
  const [tableData, setTableData] = useState(emptyTable());
  const [extracted, setExtracted] = useState(null);
  const [extractedLabel, setExtractedLabel] = useState("");
  const [error, setError] = useState("");

  const headers = { "Content-Type": "application/json", Authorization: `Bearer ${token}` };

  // --- Cell edit ---
  const updateCell = (r, c, val) => {
    const copy = tableData.map((row) => [...row]);
    copy[r][c] = val;
    setTableData(copy);
  };

  // --- Add / Delete Row ---
  const addRow = () => setTableData([...tableData, Array(tableData[0].length).fill("")]);
  const deleteRow = (r) => {
    if (tableData.length <= 1) return;
    setTableData(tableData.filter((_, i) => i !== r));
  };

  // --- Add / Delete Column ---
  const addColumn = () => setTableData(tableData.map((row) => [...row, ""]));
  const deleteColumn = (c) => {
    if (tableData[0].length <= 1) return;
    setTableData(tableData.map((row) => row.filter((_, i) => i !== c)));
  };

  // --- Delete entire table ---
  const deleteTable = () => { setTableData(emptyTable()); setExtracted(null); };

  // --- Extract Row ---
  const extractRow = async (rowIndex) => {
    try {
      const res = await fetch(`${API}/table/extract-row`, {
        method: "POST", headers,
        body: JSON.stringify({ rowIndex, tableData }),
      });
      const json = await res.json();
      if (json.error) throw new Error(json.error);
      setExtracted(json.data);
      setExtractedLabel(`Row ${rowIndex + 1}`);
      setError("");
    } catch (e) { setError(e.message); }
  };

  // --- Extract Column ---
  const extractColumn = async (colIndex) => {
    try {
      const res = await fetch(`${API}/table/extract-column`, {
        method: "POST", headers,
        body: JSON.stringify({ colIndex, tableData }),
      });
      const json = await res.json();
      if (json.error) throw new Error(json.error);
      setExtracted(json.data);
      setExtractedLabel(`Column ${colIndex + 1}`);
      setError("");
    } catch (e) { setError(e.message); }
  };

  return (
    <div style={{ padding: 16, fontFamily: "sans-serif" }}>
      <h3>Table Editor</h3>

      {/* Toolbar */}
      <div style={{ display: "flex", gap: 8, marginBottom: 12, flexWrap: "wrap" }}>
        <button onClick={addRow}>+ Add Row</button>
        <button onClick={addColumn}>+ Add Column</button>
        <button onClick={deleteTable} style={{ color: "red" }}>🗑 Delete Table</button>
      </div>

      {/* Table */}
      <div style={{ overflowX: "auto" }}>
        <table border="1" cellPadding="4" style={{ borderCollapse: "collapse", minWidth: 400 }}>
          <thead>
            <tr>
              <th></th>
              {tableData[0].map((_, c) => (
                <th key={c} style={{ background: "#f0f0f0" }}>
                  Col {c + 1}
                  <div style={{ display: "flex", gap: 4, justifyContent: "center", marginTop: 4 }}>
                    <button title="Extract column" onClick={() => extractColumn(c)}>⬇</button>
                    <button title="Delete column" onClick={() => deleteColumn(c)} style={{ color: "red" }}>✕</button>
                  </div>
                </th>
              ))}
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {tableData.map((row, r) => (
              <tr key={r}>
                <td style={{ background: "#f0f0f0", textAlign: "center" }}>Row {r + 1}</td>
                {row.map((cell, c) => (
                  <td key={c}>
                    <input
                      value={cell}
                      onChange={(e) => updateCell(r, c, e.target.value)}
                      style={{ border: "none", width: "100%", minWidth: 80 }}
                    />
                  </td>
                ))}
                <td style={{ whiteSpace: "nowrap" }}>
                  <button title="Extract row" onClick={() => extractRow(r)}>⬇ Extract</button>
                  {" "}
                  <button title="Delete row" onClick={() => deleteRow(r)} style={{ color: "red" }}>🗑</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Extracted Data */}
      {extracted && (
        <div style={{ marginTop: 16, padding: 12, background: "#f9f9f9", border: "1px solid #ddd" }}>
          <strong>Extracted {extractedLabel}:</strong>
          <pre style={{ margin: "8px 0 0" }}>{JSON.stringify(extracted, null, 2)}</pre>
          <button onClick={() => setExtracted(null)} style={{ marginTop: 8 }}>Clear</button>
        </div>
      )}

      {error && <p style={{ color: "red", marginTop: 8 }}>{error}</p>}
    </div>
  );
}
