import { useEffect, useState } from 'react';
import { Button, Card, Col, Container, Form, InputGroup, Row, Spinner } from 'react-bootstrap';
import { Link, useSearchParams } from 'react-router-dom';
import { categoryApi, productApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import { formatPrice } from '../utils/format.js';

const ProductsPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const categoryId = searchParams.get('categoryId') || '';
  const search = searchParams.get('search') || '';

  const [categories, setCategories] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchInput, setSearchInput] = useState(search);

  useEffect(() => {
    categoryApi.list().then((r) => setCategories(r.data));
  }, []);

  useEffect(() => {
    setLoading(true);
    const params = {};
    if (categoryId) params.categoryId = categoryId;
    if (search) params.search = search;
    productApi.list(params)
      .then((r) => setProducts(r.data))
      .finally(() => setLoading(false));
  }, [categoryId, search]);

  const onCategoryChange = (id) => {
    const params = new URLSearchParams();
    if (id) params.set('categoryId', id);
    if (search) params.set('search', search);
    setSearchParams(params);
  };

  const onSearchSubmit = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (categoryId) params.set('categoryId', categoryId);
    if (searchInput.trim()) params.set('search', searchInput.trim());
    setSearchParams(params);
  };

  const activeCategory = categories.find((c) => c.id == categoryId);

  return (
    <Container className="py-5">
      <h2 className="section-title">
        {activeCategory ? activeCategory.name : 'Svi proizvodi'}
      </h2>

      <Row className="mb-4 g-2">
        <Col md={4}>
          <Form.Select value={categoryId} onChange={(e) => onCategoryChange(e.target.value)}>
            <option value="">Sve kategorije</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>{c.name} ({c.productCount})</option>
            ))}
          </Form.Select>
        </Col>
        <Col md={8}>
          <Form onSubmit={onSearchSubmit}>
            <InputGroup>
              <Form.Control
                placeholder="Pretraga proizvoda..."
                value={searchInput}
                onChange={(e) => setSearchInput(e.target.value)}
              />
              <Button type="submit" className="btn-brand">
                <i className="bi bi-search"></i>
              </Button>
            </InputGroup>
          </Form>
        </Col>
      </Row>

      {loading ? (
        <div className="text-center py-5"><Spinner /></div>
      ) : products.length === 0 ? (
        <div className="text-center py-5 text-muted">
          <i className="bi bi-inbox" style={{ fontSize: '3rem' }}></i>
          <p className="mt-3">Nema pronađenih proizvoda za ove kriterijume.</p>
        </div>
      ) : (
        <Row className="g-4">
          {products.map((p) => (
            <Col lg={3} md={4} sm={6} key={p.id}>
              <Card className="card-product border-0 shadow-sm">
                <Card.Img variant="top" src={resolveImage(p.imageUrl)} loading="lazy" />
                <Card.Body className="d-flex flex-column">
                  <small className="text-muted">{p.categoryName}</small>
                  <Card.Title className="h6 mt-1">{p.name}</Card.Title>
                  {p.dimensions && <small className="text-muted">Dimenzije: {p.dimensions}</small>}
                  <div className="mt-auto pt-3">
                    <div className="d-flex justify-content-between align-items-center mb-2">
                      <span className="text-brand fw-bold fs-5">{formatPrice(p.price)}</span>
                      {p.stock > 0 ? (
                        <span className="badge bg-success">Na stanju</span>
                      ) : (
                        <span className="badge bg-secondary">Nema</span>
                      )}
                    </div>
                    <Button as={Link} to={`/proizvodi/${p.id}`} className="btn-brand w-100" size="sm">
                      Detaljnije i poručivanje
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
};

export default ProductsPage;
