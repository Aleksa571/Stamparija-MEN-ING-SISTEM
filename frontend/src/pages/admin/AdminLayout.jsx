import { Col, Container, Nav, Row } from 'react-bootstrap';
import { NavLink, Outlet } from 'react-router-dom';

const AdminLayout = () => (
  <Container fluid className="p-0">
    <Row className="g-0">
      <Col md={3} lg={2} className="admin-sidebar">
        <h6 className="text-white px-3 mb-3 text-uppercase opacity-75 small">Admin panel</h6>
        <Nav className="flex-column">
          <Nav.Link as={NavLink} to="/admin" end>
            <i className="bi bi-speedometer2 me-2"></i>Pregled
          </Nav.Link>
          <Nav.Link as={NavLink} to="/admin/proizvodi">
            <i className="bi bi-box-seam me-2"></i>Proizvodi
          </Nav.Link>
          <Nav.Link as={NavLink} to="/admin/kategorije">
            <i className="bi bi-tags me-2"></i>Kategorije
          </Nav.Link>
          <Nav.Link as={NavLink} to="/admin/porudzbine">
            <i className="bi bi-cart-check me-2"></i>Porudžbine
          </Nav.Link>
          <Nav.Link as={NavLink} to="/admin/blog">
            <i className="bi bi-journal-text me-2"></i>Blog
          </Nav.Link>
          <Nav.Link as={NavLink} to="/admin/korisnici">
            <i className="bi bi-people me-2"></i>Korisnici
          </Nav.Link>
        </Nav>
      </Col>
      <Col md={9} lg={10} className="p-4">
        <Outlet />
      </Col>
    </Row>
  </Container>
);

export default AdminLayout;
