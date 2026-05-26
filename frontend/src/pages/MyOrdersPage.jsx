import { useEffect, useState } from 'react';
import { Accordion, Alert, Badge, Card, Col, Container, Row, Spinner, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { orderApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import {
  deliveryTypeLabel,
  formatDate,
  formatPrice,
  orderStatusBadge,
  orderStatusLabel,
} from '../utils/format.js';

const MyOrdersPage = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    orderApi.myOrders().then((r) => setOrders(r.data)).finally(() => setLoading(false));
  }, []);

  return (
    <Container className="py-5">
      <h2 className="section-title">Moje porudžbine</h2>
      {loading ? (
        <div className="text-center py-5"><Spinner /></div>
      ) : orders.length === 0 ? (
        <Alert variant="info">
          <i className="bi bi-info-circle me-2"></i>
          Još uvek nemate porudžbina. <Link to="/proizvodi" className="alert-link">Pogledajte naše proizvode</Link>.
        </Alert>
      ) : (
        <Accordion alwaysOpen>
          {orders.map((o, idx) => (
            <Accordion.Item eventKey={String(idx)} key={o.id}>
              <Accordion.Header>
                <div className="d-flex flex-wrap align-items-center w-100 me-3 gap-3">
                  <strong>Porudžbina #{o.id}</strong>
                  <Badge bg={orderStatusBadge(o.status)}>{orderStatusLabel(o.status)}</Badge>
                  <span className="text-muted small">{formatDate(o.orderDate)}</span>
                  <span className="ms-auto fw-bold text-brand">{formatPrice(o.totalPrice)}</span>
                </div>
              </Accordion.Header>
              <Accordion.Body>
                <Row className="g-3 mb-3">
                  <Col md={6}>
                    <Card className="border-0 bg-light">
                      <Card.Body>
                        <small className="text-muted">NAČIN DOSTAVE</small>
                        <div className="fw-semibold">{deliveryTypeLabel(o.deliveryType)}</div>
                        {o.deliveryAddress && (
                          <>
                            <small className="text-muted d-block mt-2">ADRESA</small>
                            <div>{o.deliveryAddress}</div>
                          </>
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                  <Col md={6}>
                    <Card className="border-0 bg-light">
                      <Card.Body>
                        {o.contactPhone && (
                          <>
                            <small className="text-muted">KONTAKT</small>
                            <div className="fw-semibold">{o.contactPhone}</div>
                          </>
                        )}
                        {o.note && (
                          <>
                            <small className="text-muted d-block mt-2">NAPOMENA</small>
                            <div>{o.note}</div>
                          </>
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                </Row>
                <Table responsive hover className="align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>Proizvod</th>
                      <th>Cena</th>
                      <th>Količina</th>
                      <th className="text-end">Ukupno</th>
                    </tr>
                  </thead>
                  <tbody>
                    {o.items.map((it) => (
                      <tr key={it.id}>
                        <td>
                          <div className="d-flex align-items-center gap-2">
                            <img
                              src={resolveImage(it.productImageUrl)}
                              alt=""
                              style={{ width: 50, height: 50, objectFit: 'cover', borderRadius: 4 }}
                            />
                            <Link to={`/proizvodi/${it.productId}`} className="text-decoration-none">
                              {it.productName}
                            </Link>
                          </div>
                        </td>
                        <td>{formatPrice(it.unitPrice)}</td>
                        <td>{it.quantity}</td>
                        <td className="text-end fw-semibold">{formatPrice(it.totalPrice)}</td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot>
                    <tr>
                      <td colSpan={3} className="text-end"><strong>UKUPNO:</strong></td>
                      <td className="text-end fw-bold text-brand fs-5">{formatPrice(o.totalPrice)}</td>
                    </tr>
                  </tfoot>
                </Table>
              </Accordion.Body>
            </Accordion.Item>
          ))}
        </Accordion>
      )}
    </Container>
  );
};

export default MyOrdersPage;
