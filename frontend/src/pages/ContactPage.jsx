import { Card, Col, Container, Row } from 'react-bootstrap';

const ContactPage = () => (
  <Container className="py-5">
    <h2 className="section-title">Kontakt</h2>
    <Row className="g-4">
      <Col md={6}>
        <Card className="border-0 shadow-sm h-100">
          <Card.Body className="p-4">
            <h5 className="mb-4">Naši podaci</h5>
            <div className="mb-3 d-flex">
              <i className="bi bi-building text-brand fs-4 me-3"></i>
              <div>
                <strong>MEN-ING SISTEM DOO</strong>
                <div className="text-muted small">Štamparija sa tradicijom</div>
              </div>
            </div>
            <div className="mb-3 d-flex">
              <i className="bi bi-geo-alt-fill text-brand fs-4 me-3"></i>
              <div>
                <strong>Adresa</strong>
                <div className="text-muted">Smederevo, Srbija</div>
              </div>
            </div>
            <div className="mb-3 d-flex">
              <i className="bi bi-telephone-fill text-brand fs-4 me-3"></i>
              <div>
                <strong>Telefon</strong>
                <div className="text-muted">026/123-456</div>
              </div>
            </div>
            <div className="mb-3 d-flex">
              <i className="bi bi-envelope-fill text-brand fs-4 me-3"></i>
              <div>
                <strong>Email</strong>
                <div className="text-muted">info@meningsistem.rs</div>
              </div>
            </div>
            <div className="d-flex">
              <i className="bi bi-clock-fill text-brand fs-4 me-3"></i>
              <div>
                <strong>Radno vreme</strong>
                <div className="text-muted">Ponedeljak - Petak: 08:00 - 16:00</div>
                <div className="text-muted">Subota: 09:00 - 13:00</div>
                <div className="text-muted">Nedelja: zatvoreno</div>
              </div>
            </div>
          </Card.Body>
        </Card>
      </Col>
      <Col md={6}>
        <Card className="border-0 shadow-sm h-100">
          <Card.Body className="p-4">
            <h5 className="mb-3">Kako poručujete?</h5>
            <ol className="ps-3">
              <li className="mb-2">Pregledajte našu ponudu na stranici <a href="/proizvodi" className="text-brand">Proizvodi</a>.</li>
              <li className="mb-2">Registrujte se ili prijavite ako već imate nalog.</li>
              <li className="mb-2">Kliknite "Naruči" na proizvodu i unesite količinu i podatke za dostavu.</li>
              <li className="mb-2">Vaša porudžbina će biti pregledana i status ažuriran u realnom vremenu.</li>
              <li>Možete pratiti svoje porudžbine u sekciji <strong>Moje porudžbine</strong>.</li>
            </ol>
            <hr />
            <h6 className="mb-3">Načini dostave</h6>
            <ul className="list-unstyled">
              <li className="mb-2"><i className="bi bi-shop text-brand me-2"></i><strong>Lično preuzimanje</strong> – besplatno u našem pogonu</li>
              <li className="mb-2"><i className="bi bi-truck text-brand me-2"></i><strong>Dostava na adresu</strong> – širom okruga Smederevo</li>
              <li><i className="bi bi-box-seam text-brand me-2"></i><strong>Kurirska služba</strong> – cela Srbija</li>
            </ul>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  </Container>
);

export default ContactPage;
