import { useEffect, useState } from 'react';
import { Col, Container, Row, Spinner } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { categoryApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';

const CategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    categoryApi.list().then((r) => setCategories(r.data)).finally(() => setLoading(false));
  }, []);

  return (
    <Container className="py-5">
      <h2 className="section-title">Kategorije proizvoda</h2>
      {loading ? (
        <div className="text-center py-5"><Spinner /></div>
      ) : (
        <Row className="g-4">
          {categories.map((c) => (
            <Col md={4} sm={6} key={c.id}>
              <Link to={`/proizvodi?categoryId=${c.id}`} className="text-decoration-none">
                <div className="card-category" style={{ minHeight: 280 }}>
                  <img src={resolveImage(c.imageUrl)} alt={c.name} loading="lazy" />
                  <div className="overlay">
                    <div>
                      <h4 className="fw-bold mb-1">{c.name}</h4>
                      <small className="opacity-75">{c.productCount} proizvoda</small>
                      {c.description && (
                        <p className="small mt-2 mb-0">{c.description.substring(0, 80)}...</p>
                      )}
                    </div>
                  </div>
                </div>
              </Link>
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
};

export default CategoriesPage;
