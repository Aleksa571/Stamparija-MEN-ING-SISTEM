import { Container, Nav, Navbar as BsNavbar, NavDropdown } from 'react-bootstrap';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.jsx';

const Navbar = () => {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const onLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <BsNavbar expand="lg" className="navbar-brand-custom" variant="dark" sticky="top">
      <Container>
        <BsNavbar.Brand as={Link} to="/">
          <i className="bi bi-printer-fill me-2"></i>
          MEN-ING SISTEM
        </BsNavbar.Brand>
        <BsNavbar.Toggle aria-controls="main-nav" />
        <BsNavbar.Collapse id="main-nav">
          <Nav className="me-auto">
            <Nav.Link as={NavLink} to="/" end>Početna</Nav.Link>
            <Nav.Link as={NavLink} to="/kategorije">Kategorije</Nav.Link>
            <Nav.Link as={NavLink} to="/proizvodi">Proizvodi</Nav.Link>
            <Nav.Link as={NavLink} to="/blog">Blog</Nav.Link>
            <Nav.Link as={NavLink} to="/o-nama">O nama</Nav.Link>
            <Nav.Link as={NavLink} to="/kontakt">Kontakt</Nav.Link>
          </Nav>
          <Nav>
            {isAuthenticated ? (
              <>
                {isAdmin && (
                  <Nav.Link as={NavLink} to="/admin">
                    <i className="bi bi-shield-lock-fill me-1"></i>Admin
                  </Nav.Link>
                )}
                <NavDropdown
                  title={
                    <span>
                      <i className="bi bi-person-circle me-1"></i>
                      {user.firstName || user.username}
                    </span>
                  }
                  id="user-dropdown"
                  align="end"
                >
                  <NavDropdown.Item as={Link} to="/moje-porudzbine">
                    <i className="bi bi-bag-check me-2"></i>Moje porudžbine
                  </NavDropdown.Item>
                  <NavDropdown.Divider />
                  <NavDropdown.Item onClick={onLogout}>
                    <i className="bi bi-box-arrow-right me-2"></i>Odjava
                  </NavDropdown.Item>
                </NavDropdown>
              </>
            ) : (
              <>
                <Nav.Link as={NavLink} to="/prijava">
                  <i className="bi bi-box-arrow-in-right me-1"></i>Prijava
                </Nav.Link>
                <Nav.Link as={NavLink} to="/registracija">
                  <i className="bi bi-person-plus me-1"></i>Registracija
                </Nav.Link>
              </>
            )}
          </Nav>
        </BsNavbar.Collapse>
      </Container>
    </BsNavbar>
  );
};

export default Navbar;
