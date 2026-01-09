import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    // Redirect to login if not logged in
    if (localStorage.getItem('loggedIn') !== 'true') {
      navigate('/login');
    }
  }, [navigate]);

  return (
    <div style={styles.container}>
      <h1>Merchant Dashboard</h1>

      <div style={styles.card}>
        <p><strong>Email:</strong> test@example.com</p>
        <p><strong>API Key:</strong> key_test_abc123</p>
        <p><strong>API Secret:</strong> secret_test_xyz789</p>
      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    padding: '2rem',
    background: '#f4f6f8',
  },
  card: {
    background: '#fff',
    padding: '1.5rem',
    borderRadius: '10px',
    maxWidth: '400px',
    boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
    marginTop: '20px',
  },
};
