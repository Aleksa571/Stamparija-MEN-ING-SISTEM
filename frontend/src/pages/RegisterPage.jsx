import { useState } from 'react';
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { useToast } from '../context/ToastContext.jsx';
import { extractApiError } from '../utils/format.js';

const RegisterPage = () => {
  const { register, loading } = useAuth();
  const navigate = useNavigate();
  const toast = useToast();
  const [form, setForm] = useState({
    username: '', email: '', password: '', firstName: '', lastName: '', phone: '', address: '',
  });
  const [error, setError] = useState('');
  const [confirm, setConfirm] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    if (form.password !== confirm) {
      setError('Lozinke se ne podudaraju');
      return;
    }
    if (form.password.length < 6) {
      setError('Lozinka mora imati najmanje 6 karaktera');
      return;
    }
    try {
      await register(form);
      toast.success('Nalog uspešno kreiran! Dobrodošli.');
      navigate('/');
    } catch (err) {
      setError(extractApiError(err));
    }
  };

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8} lg={7}>
          <Card className="shadow border-0">
            <Card.Body className="p-4 p-md-5">
              <div className="text-center mb-4">
                <i className="bi bi-person-plus text-brand" style={{ fontSize: '3rem' }}></i>
                <h3 className="mt-2">Registracija</h3>
                <p className="text-muted">Kreirajte nalog za poručivanje proizvoda</p>
              </div>
              {error && <Alert variant="danger">{error}</Alert>}
              <Form onSubmit={submit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Korisničko ime *</Form.Label>
                      <Form.Control
                        value={form.username}
                        onChange={(e) => setForm({ ...form, username: e.target.value })}
                        required minLength={3} maxLength={50}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Email *</Form.Label>
                      <Form.Control
                        type="email"
                        value={form.email}
                        onChange={(e) => setForm({ ...form, email: e.target.value })}
                        required
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Lozinka *</Form.Label>
                      <Form.Control
                        type="password"
                        value={form.password}
                        onChange={(e) => setForm({ ...form, password: e.target.value })}
                        required minLength={6}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Potvrda lozinke *</Form.Label>
                      <Form.Control
                        type="password"
                        value={confirm}
                        onChange={(e) => setConfirm(e.target.value)}
                        required minLength={6}
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Ime</Form.Label>
                      <Form.Control
                        value={form.firstName}
                        onChange={(e) => setForm({ ...form, firstName: e.target.value })}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Prezime</Form.Label>
                      <Form.Control
                        value={form.lastName}
                        onChange={(e) => setForm({ ...form, lastName: e.target.value })}
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Telefon</Form.Label>
                      <Form.Control
                        value={form.phone}
                        onChange={(e) => setForm({ ...form, phone: e.target.value })}
                        placeholder="064/123-456"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Adresa</Form.Label>
                      <Form.Control
                        value={form.address}
                        onChange={(e) => setForm({ ...form, address: e.target.value })}
                        placeholder="Knez Mihailova 1, Beograd"
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <Button type="submit" className="btn-brand w-100" disabled={loading}>
                  {loading ? <Spinner size="sm" /> : 'Registruj se'}
                </Button>
              </Form>
              <hr className="my-4" />
              <div className="text-center">
                Već imate nalog?{' '}
                <Link to="/prijava" className="text-brand fw-semibold">Prijavite se</Link>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default RegisterPage;
