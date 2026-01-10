import { useEffect, useState } from "react";

function App() {

   const [orderId, setOrderId] = useState("");
   const [amount, setAmount] = useState(0);
   const [method, setMethod] = useState(null);
   const [processing, setProcessing] = useState(false);
const [paymentId, setPaymentId] = useState(null);
const [success, setSuccess] = useState(false);
const [error, setError] = useState(false);


useEffect(() => {
  const params = new URLSearchParams(window.location.search)
  const id = params.get("order_id")

  if (!id) return
  setOrderId(id)

  console.log("Fetching order for id:", id); // 🔹

  fetch(`http://localhost:8000/api/v1/orders/${id}/public`)
    .then(res => {
      console.log("Response status:", res.status); // 🔹
      return res.json()
    })
    .then(data => {
      console.log("Fetched data:", data); // 🔹
      setAmount(data.amount)
    })
    .catch(err => {
      console.error("Order fetch failed", err)
    })
}, []);

useEffect(() => {
  if (!paymentId) return;

  const interval = setInterval(async () => {
    const res = await fetch(
      `http://localhost:8000/api/v1/payments/${paymentId}/public`
    );
    const data = await res.json();

    if (data.status === "success") {
      setProcessing(false);
      setSuccess(true);
      clearInterval(interval);
    }

    if (data.status === "failed") {
      setProcessing(false);
      setError(true);
      clearInterval(interval);
    }
  }, 2000);

  return () => clearInterval(interval);
}, [paymentId]);


const handlePay = async (e) => {
  e.preventDefault();
  setProcessing(true);

  try {
    const res = await fetch("http://localhost:8000/api/v1/payments/public", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        order_id: orderId,
        method
      })
    });

    const data = await res.json();
    setPaymentId(data.id);
  } catch {
    setProcessing(false);
    setError(true);
  }
};


  return (
    <div data-test-id="checkout-container">

      {/* Order Summary */}
      <div data-test-id="order-summary">
        <h2>Complete Payment</h2>
        <div>
          <span>Amount: </span>
          <span data-test-id="order-amount">₹{amount / 100}</span>
        </div>
        <div>
          <span>Order ID: </span>
          <span data-test-id="order-id">{orderId}</span>
        </div>
      </div>

      {/* Payment Method Selection */}
      <div data-test-id="payment-methods">
        <button
  data-test-id="method-upi"
  onClick={() => setMethod("upi")}
>
  UPI
</button>

<button
  data-test-id="method-card"
  onClick={() => setMethod("card")}
>
  Card
</button>


      </div>

      {/* UPI Form */}
      {method === "upi" && (
  <form data-test-id="upi-form" onSubmit={handlePay}>
    <input data-test-id="vpa-input" placeholder="username@bank" />
    <button data-test-id="pay-button">Pay ₹{amount / 100}</button>
  </form>
)}

      {/* Card Form */}
      {method === "card" && (
  <form data-test-id="card-form" onSubmit={handlePay}>
    <input data-test-id="card-number-input" placeholder="Card Number" />
    <input data-test-id="expiry-input" placeholder="MM/YY" />
    <input data-test-id="cvv-input" placeholder="CVV" />
    <input data-test-id="cardholder-name-input" placeholder="Name on Card" />
    <button data-test-id="pay-button">Pay ₹{amount / 100}</button>
  </form>
)}

      {/* Processing State */}
      {processing && (
  <div data-test-id="processing-state">
    <span data-test-id="processing-message">
      Processing payment...
    </span>
  </div>
)}


      {/* Success State */}
      {success && (
  <div data-test-id="success-state">
    <h2>Payment Successful!</h2>
    <span data-test-id="payment-id">{paymentId}</span>
    <span data-test-id="success-message">
      Your payment has been processed successfully
    </span>
  </div>
)}


      {/* Error State */}
      {error && (
  <div data-test-id="error-state">
    <h2>Payment Failed</h2>
    <span data-test-id="error-message">
      Payment could not be processed
    </span>
    <button data-test-id="retry-button" onClick={() => window.location.reload()}>
      Try Again
    </button>
  </div>
)}


    </div>
  )
}

export default App
