import { useEffect, useState } from 'react';
import { Alert, Badge, Button, Col, Container, Row, Spinner } from 'react-bootstrap';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { productApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import { formatPrice } from '../utils/format.js';
import { useAuth } from '../context/AuthContext.jsx';

const ProductDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    productApi.get(id)
      .then((r) => setProduct(r.data))
      .catch((e) => setError('Proizvod nije pronađen'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div className="text-center py-5"><Spinner /></div>;
  if (error) return <Container className="py-5"><Alert variant="danger">{error}</Alert></Container>;
  if (!product) return null;

  const onOrder = () => {
    if (!isAuthenticated) {
      navigate('/prijava', { state: { from: { pathname: `/poruci/${product.id}` } } });
      return;
    }
    navigate(`/poruci/${product.id}`);
  };

  return (
    <Container className="py-5">
      <nav aria-label="breadcrumb" className="mb-4">
        <ol className="breadcrumb">
          <li className="breadcrumb-item"><Link to="/">Početna</Link></li>
          <li className="breadcrumb-item"><Link to="/proizvodi">Proizvodi</Link></li>
          <li className="breadcrumb-item">
            <Link to={`/proizvodi?categoryId=${product.categoryId}`}>{product.categoryName}</Link>
          </li>
          <li className="breadcrumb-item active">{product.name}</li>
        </ol>
      </nav>
      <Row className="g-5">
        <Col md={6}>
          <img src={resolveImage(product.imageUrl)} alt={product.name} className="product-detail-image" />
        </Col>
        <Col md={6}>
          <Badge bg="secondary" className="mb-2">{product.categoryName}</Badge>
          <h2 className="mb-3">{product.name}</h2>
          <div className="d-flex align-items-center gap-3 mb-3">
            <span className="text-brand fw-bold fs-2">{formatPrice(product.price)}</span>
            {product.available && product.stock > 0 ? (
              <Badge bg="success">Na stanju ({product.stock})</Badge>
            ) : (
              <Badge bg="danger">Nedostupno</Badge>
            )}
          </div>
          {product.dimensions && (
            <p><strong>Dimenzije:</strong> {product.dimensions}</p>
          )}
          <hr />
          <h6>Opis proizvoda</h6>
          <p className="text-muted">{product.description || 'Nema dostupnog opisa.'}</p>
          <hr />
          <div className="d-grid gap-2">
            <Button
              className="btn-brand btn-lg"
              onClick={onOrder}
              disabled={!product.available || product.stock <= 0}
            >
              <i className="bi bi-bag-check me-2"></i>
              {isAuthenticated ? 'Naruči ovaj proizvod' : 'Prijavi se i naruči'}
            </Button>
            <Button as={Link} to="/proizvodi" variant="outline-secondary">
              <i className="bi bi-arrow-left me-2"></i>Nazad na proizvode
            </Button>
          </div>
          <div className="mt-4 small text-muted">
            <i className="bi bi-truck me-2"></i>Dostava na adresu, lično preuzimanje ili slanje kurirskom službom.
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default ProductDetailPage;
