import { Col, Container, Row } from 'react-bootstrap';

const FeatureStrip = () => (
  <div className="feature-strip">
    <Container>
      <Row className="g-3">
        <Col md={3} sm={6} className="feature-item">
          <i className="bi bi-truck"></i>Brza dostava
        </Col>
        <Col md={3} sm={6} className="feature-item">
          <i className="bi bi-award"></i>Vrhunski kvalitet
        </Col>
        <Col md={3} sm={6} className="feature-item">
          <i className="bi bi-clock-history"></i>Tradicija stara deceniju
        </Col>
        <Col md={3} sm={6} className="feature-item">
          <i className="bi bi-currency-dollar"></i>Konkurentne cene
        </Col>
      </Row>
    </Container>
  </div>
);

export default FeatureStrip;
