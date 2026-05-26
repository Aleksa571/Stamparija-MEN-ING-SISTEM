import { useEffect, useState } from 'react';
import { Card, Col, Container, Row, Button, Spinner } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { categoryApi, productApi, blogApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import { formatPrice, formatDateOnly } from '../utils/format.js';
import FeatureStrip from '../components/layout/FeatureStrip.jsx';
import Reveal from '../components/common/Reveal.jsx';

const heroVariants = {
  hidden: { opacity: 0, y: 30 },
  visible: (i = 0) => ({
    opacity: 1,
    y: 0,
    transition: { duration: 0.6, delay: 0.1 + i * 0.12, ease: 'easeOut' },
  }),
};

const HomePage = () => {
  const [categories, setCategories] = useState([]);
  const [products, setProducts] = useState([]);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([categoryApi.list(), productApi.list(), blogApi.list()])
      .then(([c, p, b]) => {
        setCategories(c.data);
        setProducts(p.data.slice(0, 8));
        setPosts(b.data.slice(0, 3));
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <section className="hero">
        <Container>
          <motion.h1 custom={0} initial="hidden" animate="visible" variants={heroVariants}>
            MEN-ING SISTEM DOO
          </motion.h1>
          <motion.p
            className="lead mb-2"
            custom={1}
            initial="hidden"
            animate="visible"
            variants={heroVariants}
          >
            Štamparija sa tradicijom u Smederevu
          </motion.p>
          <motion.p
            className="mb-4 opacity-90"
            custom={2}
            initial="hidden"
            animate="visible"
            variants={heroVariants}
          >
            Kartonska ambalaža, ofset štampa, blokovska roba.
            <br />
            Od 12 štamparija u gradu, ostale su samo 2 – nije slučajno.
          </motion.p>
          <motion.div custom={3} initial="hidden" animate="visible" variants={heroVariants}>
            <Button as={Link} to="/proizvodi" className="btn-brand btn-lg me-2">
              <i className="bi bi-grid me-2"></i>Pogledaj proizvode
            </Button>
            <Button as={Link} to="/kontakt" variant="outline-light" size="lg">
              <i className="bi bi-telephone me-2"></i>Kontakt
            </Button>
          </motion.div>
        </Container>
      </section>

      <FeatureStrip />

      <Container className="py-5">
        <Reveal>
          <div className="text-center mb-4">
            <h2 className="section-title">Naše kategorije</h2>
            <p className="text-muted">Pogledajte šta sve štampamo</p>
          </div>
        </Reveal>
        {loading ? (
          <div className="text-center py-5"><Spinner /></div>
        ) : (
          <Row className="g-3">
            {categories.map((c, idx) => (
              <Col md={4} sm={6} key={c.id}>
                <Reveal delay={idx * 0.08}>
                  <Link to={`/proizvodi?categoryId=${c.id}`} className="text-decoration-none">
                    <div className="card-category">
                      <img src={resolveImage(c.imageUrl)} alt={c.name} loading="lazy" />
                      <div className="overlay">
                        <div>
                          <h5 className="fw-bold mb-1">{c.name}</h5>
                          <small className="opacity-75">{c.productCount} proizvoda</small>
                        </div>
                      </div>
                    </div>
                  </Link>
                </Reveal>
              </Col>
            ))}
          </Row>
        )}
      </Container>

      <div style={{ backgroundColor: 'white' }}>
        <Container className="py-5">
          <Reveal>
            <div className="text-center mb-4">
              <h2 className="section-title">Izdvajamo iz ponude</h2>
            </div>
          </Reveal>
          {loading ? (
            <div className="text-center"><Spinner /></div>
          ) : (
            <Row className="g-4">
              {products.map((p, idx) => (
                <Col lg={3} md={4} sm={6} key={p.id}>
                  <Reveal delay={idx * 0.06}>
                    <Card className="card-product border-0 shadow-sm">
                      <Card.Img variant="top" src={resolveImage(p.imageUrl)} loading="lazy" />
                      <Card.Body className="d-flex flex-column">
                        <small className="text-muted">{p.categoryName}</small>
                        <Card.Title className="h6 mt-1">{p.name}</Card.Title>
                        <div className="mt-auto pt-2">
                          <div className="d-flex justify-content-between align-items-center">
                            <span className="text-brand fw-bold">{formatPrice(p.price)}</span>
                            <Button as={Link} to={`/proizvodi/${p.id}`} size="sm" variant="outline-dark">
                              Detaljnije
                            </Button>
                          </div>
                        </div>
                      </Card.Body>
                    </Card>
                  </Reveal>
                </Col>
              ))}
            </Row>
          )}
          <Reveal delay={0.15}>
            <div className="text-center mt-4">
              <Button as={Link} to="/proizvodi" variant="outline-dark">
                Svi proizvodi <i className="bi bi-arrow-right ms-1"></i>
              </Button>
            </div>
          </Reveal>
        </Container>
      </div>

      <Container className="py-5">
        <Reveal>
          <div className="text-center mb-4">
            <h2 className="section-title">Najnovije sa bloga</h2>
          </div>
        </Reveal>
        <Row className="g-4">
          {posts.map((post, idx) => (
            <Col md={4} key={post.id}>
              <Reveal delay={idx * 0.1}>
                <Card className="border-0 shadow-sm h-100">
                  {post.imageUrl && (
                    <Card.Img variant="top" src={resolveImage(post.imageUrl)} style={{ height: 200, objectFit: 'cover' }} />
                  )}
                  <Card.Body>
                    <small className="text-muted">{formatDateOnly(post.createdAt)}</small>
                    <Card.Title className="h6 mt-1">{post.title}</Card.Title>
                    <Card.Text className="small text-muted">
                      {post.content.substring(0, 120)}...
                    </Card.Text>
                    <Link to={`/blog/${post.id}`} className="text-brand fw-semibold small">
                      Pročitaj više <i className="bi bi-arrow-right"></i>
                    </Link>
                  </Card.Body>
                </Card>
              </Reveal>
            </Col>
          ))}
        </Row>
      </Container>
    </>
  );
};

export default HomePage;
