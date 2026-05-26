import { useEffect, useState } from 'react';
import { Alert, Container, Spinner } from 'react-bootstrap';
import { Link, useParams } from 'react-router-dom';
import { blogApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import { formatDate } from '../utils/format.js';

const BlogDetailPage = () => {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    blogApi.get(id)
      .then((r) => setPost(r.data))
      .catch(() => setError('Objava nije pronađena'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div className="text-center py-5"><Spinner /></div>;
  if (error) return <Container className="py-5"><Alert variant="danger">{error}</Alert></Container>;

  return (
    <Container className="py-5" style={{ maxWidth: 800 }}>
      <Link to="/blog" className="text-brand small">
        <i className="bi bi-arrow-left me-1"></i>Nazad na blog
      </Link>
      <h1 className="mt-3 mb-3">{post.title}</h1>
      <div className="d-flex gap-3 text-muted small mb-4">
        <span><i className="bi bi-person me-1"></i>{post.authorFullName || post.authorUsername}</span>
        <span><i className="bi bi-calendar-event me-1"></i>{formatDate(post.createdAt)}</span>
        {post.updatedAt !== post.createdAt && (
          <span><i className="bi bi-pencil me-1"></i>Ažurirano: {formatDate(post.updatedAt)}</span>
        )}
      </div>
      {post.imageUrl && (
        <img src={resolveImage(post.imageUrl)} alt={post.title} className="w-100 mb-4 rounded shadow-sm" style={{ maxHeight: 400, objectFit: 'cover' }} />
      )}
      <div style={{ whiteSpace: 'pre-line', lineHeight: 1.8 }}>
        {post.content}
      </div>
    </Container>
  );
};

export default BlogDetailPage;
