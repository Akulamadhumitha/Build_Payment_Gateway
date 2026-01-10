import { useEffect, useState } from "react";

export default function Dashboard() {
  const [stats, setStats] = useState({
    totalTransactions: 0,
    totalAmount: 0,
    successRate: 0,
  });

  useEffect(() => {
    fetch("http://localhost:8000/api/dashboard/stats")
      .then((res) => res.json())
      .then((data) => setStats(data))
      .catch((err) => console.error("Stats fetch failed", err));
  }, []);

  return (
    <div
      data-test-id="dashboard"
      style={{ padding: "40px", fontFamily: "Arial" }}
    >
      <h1>Merchant Dashboard</h1>

      {/* API Credentials */}
      <div data-test-id="api-credentials" style={cardStyle}>
        <div>
          <label>API Key</label>
          <div data-test-id="api-key">key_test_abc123</div>
        </div>

        <div>
          <label>API Secret</label>
          <div data-test-id="api-secret">secret_test_xyz789</div>
        </div>
      </div>

      {/* Stats */}
      <div
        data-test-id="stats-container"
        style={{ display: "flex", gap: "20px", marginTop: "30px" }}
      >
        <div style={statBox}>
          <div data-test-id="total-transactions">
            {stats.totalTransactions}
          </div>
          <small>Total Transactions</small>
        </div>

        <div style={statBox}>
          <div data-test-id="total-amount">
            ₹{stats.totalAmount.toLocaleString()}
          </div>
          <small>Total Amount</small>
        </div>

        <div style={statBox}>
          <div data-test-id="success-rate">
            {stats.successRate}%
          </div>
          <small>Success Rate</small>
        </div>
      </div>
    </div>
  );
}

const cardStyle = {
  background: "#f9f9f9",
  padding: "20px",
  borderRadius: "10px",
  maxWidth: "400px",
};

const statBox = {
  background: "#ffffff",
  padding: "20px",
  borderRadius: "10px",
  minWidth: "150px",
  textAlign: "center",
  boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
};
