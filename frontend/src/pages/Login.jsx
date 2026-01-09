import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleSubmit = (e) => {
    e.preventDefault()

    // Deliverable 1: simple validation
    if (email === 'test@example.com') {
      localStorage.setItem('loggedIn', 'true')
      navigate('/dashboard')
    } else {
      alert('Invalid email')
    }
  }

  return (
    <div style={styles.container}>
      <form data-test-id="login-form" onSubmit={handleSubmit} style={styles.card}>
        <h2>Payment Gateway Login</h2>

        <input
          data-test-id="email-input"
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={styles.input}
        />

        <input
          data-test-id="password-input"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={styles.input}
        />

        <button
          data-test-id="login-button"
          type="submit"
          style={styles.button}
        >
          Login
        </button>
      </form>
    </div>
  )
}

const styles = {
  container: {
    minHeight: '100vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    background: 'linear-gradient(135deg, #0f2027, #203a43, #2c5364)',
  },
  card: {
    background: '#fff',
    padding: '2rem',
    borderRadius: '12px',
    width: '320px',
    boxShadow: '0 10px 25px rgba(0,0,0,0.2)',
  },
  input: {
    width: '100%',
    padding: '10px',
    marginBottom: '12px',
    borderRadius: '6px',
    border: '1px solid #ccc',
  },
  button: {
    width: '100%',
    padding: '10px',
    background: '#2c5364',
    color: '#fff',
    border: 'none',
    borderRadius: '6px',
    cursor: 'pointer',
  },
}
