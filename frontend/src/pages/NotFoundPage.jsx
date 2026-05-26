import { Container, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const NotFoundPage = () => (
  <Container className="py-5 text-center">
    <h1 style={{ fontSize: '6rem' }} className="text-brand fw-bold">404</h1>
    <h3>Stranica nije pronađena</h3>
    <p className="text-muted">Stranica koju tražite ne postoji ili je premeštena.</p>
    <Button as={Link} to="/" className="btn-brand">
      <i className="bi bi-house-fill me-2"></i>Nazad na početnu
    </Button>
  </Container>
);

export default NotFoundPage;
