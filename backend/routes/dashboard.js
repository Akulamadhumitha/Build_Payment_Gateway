import express from "express";
import pool from "../db.js";

const router = express.Router();

router.get("/stats", async (req, res) => {
  try {
    const total = await pool.query(
      "SELECT COUNT(*) FROM payments"
    );

    const success = await pool.query(
      "SELECT COUNT(*) FROM payments WHERE status = 'success'"
    );

    const amount = await pool.query(
      "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'success'"
    );

    const totalCount = Number(total.rows[0].count);
    const successCount = Number(success.rows[0].count);
    const totalAmount = Number(amount.rows[0].sum);

    const successRate =
      totalCount === 0 ? 0 : Math.round((successCount / totalCount) * 100);

    res.json({
      totalTransactions: totalCount,
      totalAmount,
      successRate,
    });
  } catch (err) {
    res.status(500).json({ error: "Failed to fetch stats" });
  }
});

export default router;
