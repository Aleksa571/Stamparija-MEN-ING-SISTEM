import { useEffect, useState } from 'react';
import { Card, Col, Row, Spinner } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { blogApi, categoryApi, orderApi, productApi, userApi } from '../../api/services.js';
import { formatPrice } from '../../utils/format.js';

const StatCard = ({ icon, title, value, color, to }) => (
  <Col md={4} sm={6}>
    <Card className="border-0 shadow-sm h-100" as={Link} to={to} style={{ textDecoration: 'none' }}>
      <Card.Body>
        <div className="d-flex align-items-center">
          <div className={`bg-${color} text-white rounded-circle d-flex align-items-center justify-content-center me-3`}
               style={{ width: 60, height: 60, fontSize: '1.5rem' }}>
            <i className={`bi ${icon}`}></i>
          </div>
          <div>
            <div className="text-muted small">{title}</div>
            <div className="fs-3 fw-bold">{value}</div>
          </div>
        </div>
      </Card.Body>
    </Card>
  </Col>
);

const AdminDashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      productApi.list(),
      categoryApi.list(),
      orderApi.list(),
      blogApi.list(),
      userApi.list(),
    ])
      .then(([p, c, o, b, u]) => {
        const totalRevenue = o.data.reduce((sum, ord) => sum + Number(ord.totalPrice), 0);
        const pending = o.data.filter((ord) => ord.status === 'PRIMLJENA' || ord.status === 'U_IZRADI').length;
        setStats({
          products: p.data.length,
          categories: c.data.length,
          orders: o.data.length,
          posts: b.data.length,
          users: u.data.length,
          revenue: totalRevenue,
          pending,
        });
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="text-center py-5"><Spinner /></div>;

  return (
    <>
      <h3 className="mb-4">Dobrodošli u admin panel</h3>
      <Row className="g-3 mb-4">
        <StatCard icon="bi-box-seam" title="Proizvoda" value={stats.products} color="primary" to="/admin/proizvodi" />
        <StatCard icon="bi-tags" title="Kategorija" value={stats.categories} color="warning" to="/admin/kategorije" />
        <StatCard icon="bi-cart-check" title="Porudžbina" value={stats.orders} color="success" to="/admin/porudzbine" />
        <StatCard icon="bi-journal-text" title="Blog objava" value={stats.posts} color="info" to="/admin/blog" />
        <StatCard icon="bi-people" title="Korisnika" value={stats.users} color="dark" to="/admin/korisnici" />
        <StatCard icon="bi-hourglass-split" title="Na čekanju" value={stats.pending} color="danger" to="/admin/porudzbine" />
      </Row>
      <Card className="border-0 shadow-sm">
        <Card.Body>
          <h6 className="text-muted text-uppercase small mb-2">Ukupan promet (sve porudžbine)</h6>
          <div className="fs-2 fw-bold text-brand">{formatPrice(stats.revenue)}</div>
        </Card.Body>
      </Card>
    </>
  );
};

export default AdminDashboard;
