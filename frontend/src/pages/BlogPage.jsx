import { useEffect, useState } from 'react';
import { Alert, Card, Col, Container, Row, Spinner } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { blogApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import { formatDate } from '../utils/format.js';

const BlogPage = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    blogApi.list().then((r) => setPosts(r.data)).finally(() => setLoading(false));
  }, []);

  return (
    <Container className="py-5">
      <h2 className="section-title">Blog i obaveštenja</h2>
      <p className="text-muted mb-4">Pratite najnovije vesti, ponude i promene u našoj štampariji.</p>
      {loading ? (
        <div className="text-center py-5"><Spinner /></div>
      ) : posts.length === 0 ? (
        <Alert variant="info">Trenutno nema objava.</Alert>
      ) : (
        <Row className="g-4">
          {posts.map((post) => (
            <Col md={6} lg={4} key={post.id}>
              <Card className="border-0 shadow-sm h-100">
                {post.imageUrl && (
                  <Card.Img variant="top" src={resolveImage(post.imageUrl)} style={{ height: 220, objectFit: 'cover' }} />
                )}
                <Card.Body className="d-flex flex-column">
                  <small className="text-muted">
                    <i className="bi bi-calendar-event me-1"></i>{formatDate(post.createdAt)}
                  </small>
                  <Card.Title className="h6 mt-2">{post.title}</Card.Title>
                  <Card.Text className="small text-muted flex-grow-1">
                    {post.content.length > 150 ? post.content.substring(0, 150) + '...' : post.content}
                  </Card.Text>
                  <div className="d-flex justify-content-between align-items-center mt-2">
                    <small className="text-muted">
                      <i className="bi bi-person me-1"></i>{post.authorFullName || post.authorUsername}
                    </small>
                    <Link to={`/blog/${post.id}`} className="text-brand fw-semibold small">
                      Pročitaj <i className="bi bi-arrow-right"></i>
                    </Link>
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

export default BlogPage;
