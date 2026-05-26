import { useEffect, useState } from 'react';
import { Badge, Button, Card, Col, Form, Modal, Row, Spinner, Table } from 'react-bootstrap';
import { orderApi } from '../../api/services.js';
import { useToast } from '../../context/ToastContext.jsx';
import {
  deliveryTypeLabel,
  extractApiError,
  formatDate,
  formatPrice,
  orderStatusBadge,
  orderStatusLabel,
} from '../../utils/format.js';

const STATUSES = ['PRIMLJENA', 'U_IZRADI', 'SPREMNA', 'ISPORUCENA', 'OTKAZANA'];

const AdminOrders = () => {
  const toast = useToast();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [show, setShow] = useState(false);
  const [active, setActive] = useState(null);

  const load = () => {
    setLoading(true);
    orderApi.list().then((r) => setOrders(r.data)).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const updateStatus = async (order, status) => {
    try {
      await orderApi.updateStatus(order.id, status);
      toast.success(`Status porudžbine #${order.id} promenjen na ${orderStatusLabel(status)}`);
      load();
      if (active && active.id === order.id) {
        setActive({ ...active, status });
      }
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  const del = async (order) => {
    if (!window.confirm(`Obrisati porudžbinu #${order.id}?`)) return;
    try {
      await orderApi.delete(order.id);
      toast.success('Porudžbina obrisana');
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  return (
    <>
      <h3 className="mb-4">Porudžbine</h3>
      {loading ? <Spinner /> : (
        <Table responsive hover className="bg-white shadow-sm align-middle">
          <thead className="table-light">
            <tr>
              <th>#</th>
              <th>Datum</th>
              <th>Kupac</th>
              <th>Dostava</th>
              <th>Stavki</th>
              <th>Ukupno</th>
              <th>Status</th>
              <th className="text-end">Akcije</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((o) => (
              <tr key={o.id}>
                <td className="fw-bold">#{o.id}</td>
                <td className="small">{formatDate(o.orderDate)}</td>
                <td>
                  <div className="fw-semibold">{o.userFullName || o.username}</div>
                  <small className="text-muted">{o.username}</small>
                </td>
                <td><Badge bg="info">{deliveryTypeLabel(o.deliveryType)}</Badge></td>
                <td>{o.items.length}</td>
                <td className="fw-bold text-brand">{formatPrice(o.totalPrice)}</td>
                <td>
                  <Form.Select size="sm" value={o.status}
                               onChange={(e) => updateStatus(o, e.target.value)}
                               style={{ minWidth: 130 }}>
                    {STATUSES.map((s) => <option key={s} value={s}>{orderStatusLabel(s)}</option>)}
                  </Form.Select>
                </td>
                <td className="text-end">
                  <Button size="sm" variant="outline-primary" className="me-2"
                          onClick={() => { setActive(o); setShow(true); }}>
                    <i className="bi bi-eye"></i>
                  </Button>
                  <Button size="sm" variant="outline-danger" onClick={() => del(o)}>
                    <i className="bi bi-trash"></i>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <Modal show={show} onHide={() => setShow(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Detalji porudžbine #{active?.id}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {active && (
            <>
              <Row className="g-3 mb-3">
                <Col md={6}>
                  <Card className="border-0 bg-light">
                    <Card.Body>
                      <small className="text-muted">KUPAC</small>
                      <div className="fw-semibold">{active.userFullName || active.username}</div>
                      <small className="text-muted">{active.username}</small>
                    </Card.Body>
                  </Card>
                </Col>
                <Col md={6}>
                  <Card className="border-0 bg-light">
                    <Card.Body>
                      <small className="text-muted">STATUS</small>
                      <div><Badge bg={orderStatusBadge(active.status)} className="fs-6">{orderStatusLabel(active.status)}</Badge></div>
                      <small className="text-muted">{formatDate(active.orderDate)}</small>
                    </Card.Body>
                  </Card>
                </Col>
                <Col md={6}>
                  <Card className="border-0 bg-light">
                    <Card.Body>
                      <small className="text-muted">DOSTAVA</small>
                      <div className="fw-semibold">{deliveryTypeLabel(active.deliveryType)}</div>
                      {active.deliveryAddress && <div>{active.deliveryAddress}</div>}
                      {active.contactPhone && <small className="text-muted">Tel: {active.contactPhone}</small>}
                    </Card.Body>
                  </Card>
                </Col>
                {active.note && (
                  <Col md={6}>
                    <Card className="border-0 bg-light">
                      <Card.Body>
                        <small className="text-muted">NAPOMENA</small>
                        <div>{active.note}</div>
                      </Card.Body>
                    </Card>
                  </Col>
                )}
              </Row>
              <Table size="sm" hover>
                <thead className="table-light">
                  <tr><th>Proizvod</th><th>Cena</th><th>Količina</th><th className="text-end">Ukupno</th></tr>
                </thead>
                <tbody>
                  {active.items.map((it) => (
                    <tr key={it.id}>
                      <td>{it.productName}</td>
                      <td>{formatPrice(it.unitPrice)}</td>
                      <td>{it.quantity}</td>
                      <td className="text-end fw-semibold">{formatPrice(it.totalPrice)}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td colSpan={3} className="text-end"><strong>UKUPNO:</strong></td>
                    <td className="text-end fw-bold text-brand fs-5">{formatPrice(active.totalPrice)}</td>
                  </tr>
                </tfoot>
              </Table>
            </>
          )}
        </Modal.Body>
      </Modal>
    </>
  );
};

export default AdminOrders;
