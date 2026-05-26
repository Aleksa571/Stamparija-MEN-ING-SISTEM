import { useState } from 'react';
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { useToast } from '../context/ToastContext.jsx';
import { extractApiError } from '../utils/format.js';

const LoginPage = () => {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const toast = useToast();
  const from = location.state?.from?.pathname || '/';

  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await login(form.username, form.password);
      toast.success('Uspešno ste se prijavili!');
      navigate(from, { replace: true });
    } catch (err) {
      setError(extractApiError(err));
    }
  };

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={6} lg={5}>
          <Card className="shadow border-0">
            <Card.Body className="p-4 p-md-5">
              <div className="text-center mb-4">
                <i className="bi bi-box-arrow-in-right text-brand" style={{ fontSize: '3rem' }}></i>
                <h3 className="mt-2">Prijava</h3>
                <p className="text-muted">Dobrodošli nazad u MEN-ING SISTEM</p>
              </div>
              {error && <Alert variant="danger">{error}</Alert>}
              <Form onSubmit={submit}>
                <Form.Group className="mb-3">
                  <Form.Label>Korisničko ime ili email</Form.Label>
                  <Form.Control
                    type="text"
                    value={form.username}
                    onChange={(e) => setForm({ ...form, username: e.target.value })}
                    required
                    autoFocus
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Lozinka</Form.Label>
                  <Form.Control
                    type="password"
                    value={form.password}
                    onChange={(e) => setForm({ ...form, password: e.target.value })}
                    required
                  />
                </Form.Group>
                <Button type="submit" className="btn-brand w-100" disabled={loading}>
                  {loading ? <Spinner size="sm" /> : 'Prijavi se'}
                </Button>
              </Form>
              <hr className="my-4" />
              <div className="text-center">
                Nemate nalog?{' '}
                <Link to="/registracija" className="text-brand fw-semibold">Registrujte se</Link>
              </div>
              <div className="text-center mt-3 small text-muted">
                <i className="bi bi-info-circle me-1"></i>
                Test nalozi: <code>admin</code> / <code>admin123</code> ili <code>kupac</code> / <code>kupac123</code>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default LoginPage;
