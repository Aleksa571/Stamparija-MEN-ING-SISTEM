import { Container, Nav, Navbar as BsNavbar, NavDropdown } from 'react-bootstrap';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '../../context/AuthContext.jsx';

const navItems = [
  { to: '/', label: 'Početna', end: true },
  { to: '/kategorije', label: 'Kategorije' },
  { to: '/proizvodi', label: 'Proizvodi' },
  { to: '/blog', label: 'Blog' },
  { to: '/o-nama', label: 'O nama' },
  { to: '/kontakt', label: 'Kontakt' },
];

const AnimatedNavLink = ({ to, label, end = false, icon }) => (
  <motion.div
    className="nav-item-wrap"
    whileHover={{ y: -2 }}
    whileTap={{ scale: 0.94 }}
    transition={{ type: 'spring', stiffness: 400, damping: 22 }}
  >
    <Nav.Link as={NavLink} to={to} end={end} className="nav-link-animated">
      {icon && <i className={`bi ${icon} me-1`}></i>}
      <span>{label}</span>
    </Nav.Link>
  </motion.div>
);

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
        <motion.div
          whileHover={{ scale: 1.04 }}
          whileTap={{ scale: 0.97 }}
          transition={{ type: 'spring', stiffness: 350, damping: 20 }}
        >
          <BsNavbar.Brand as={Link} to="/">
            <i className="bi bi-printer-fill me-2"></i>
            MEN-ING SISTEM
          </BsNavbar.Brand>
        </motion.div>
        <BsNavbar.Toggle aria-controls="main-nav" />
        <BsNavbar.Collapse id="main-nav">
          <Nav className="me-auto">
            {navItems.map((item) => (
              <AnimatedNavLink key={item.to} {...item} />
            ))}
          </Nav>
          <Nav>
            {isAuthenticated ? (
              <>
                {isAdmin && <AnimatedNavLink to="/admin" label="Admin" icon="bi-shield-lock-fill" />}
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
                <AnimatedNavLink to="/prijava" label="Prijava" icon="bi-box-arrow-in-right" />
                <AnimatedNavLink to="/registracija" label="Registracija" icon="bi-person-plus" />
              </>
            )}
          </Nav>
        </BsNavbar.Collapse>
      </Container>
    </BsNavbar>
  );
};

export default Navbar;
