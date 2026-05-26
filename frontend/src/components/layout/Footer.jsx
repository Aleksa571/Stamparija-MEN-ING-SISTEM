import { Col, Container, Row } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Footer = () => (
  <footer className="site-footer mt-5">
    <Container>
      <Row className="gy-4">
        <Col md={4}>
          <h5 className="text-white fw-bold mb-3">
            <i className="bi bi-printer-fill me-2 text-warning"></i>
            MEN-ING SISTEM DOO
          </h5>
          <p className="mb-1">Štamparija sa tradicijom u Smederevu.</p>
          <p className="mb-1">Specijalizovani za kartonsku ambalažu, ofset i digitalnu štampu.</p>
          <p className="small mt-3 mb-0 opacity-75">Od 12 štamparija u gradu, ostale su samo 2 – jedna od njih smo mi.</p>
        </Col>
        <Col md={4}>
          <h6 className="text-white fw-bold mb-3">Ponuda</h6>
          <ul className="list-unstyled">
            <li><Link to="/kategorije">Kategorije proizvoda</Link></li>
            <li><Link to="/proizvodi">Svi proizvodi</Link></li>
            <li><Link to="/blog">Blog i obaveštenja</Link></li>
            <li><Link to="/o-nama">O nama</Link></li>
          </ul>
        </Col>
        <Col md={4}>
          <h6 className="text-white fw-bold mb-3">Kontakt</h6>
          <p className="mb-1"><i className="bi bi-geo-alt-fill me-2 text-warning"></i>Smederevo, Srbija</p>
          <p className="mb-1"><i className="bi bi-telephone-fill me-2 text-warning"></i>026/123-456</p>
          <p className="mb-1"><i className="bi bi-envelope-fill me-2 text-warning"></i>info@meningsistem.rs</p>
          <p className="mb-0"><i className="bi bi-clock-fill me-2 text-warning"></i>Pon - Pet: 08:00 - 16:00</p>
        </Col>
      </Row>
      <hr className="my-4" style={{ borderColor: 'rgba(255,255,255,0.15)' }} />
      <div className="text-center small opacity-75">
        &copy; {new Date().getFullYear()} MEN-ING SISTEM DOO. Sva prava zadržana. | Završni projekat - Internet softverske arhitekture
      </div>
    </Container>
  </footer>
);

export default Footer;
