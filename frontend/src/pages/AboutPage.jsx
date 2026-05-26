import { Col, Container, Row } from 'react-bootstrap';

const AboutPage = () => (
  <Container className="py-5">
    <h2 className="section-title">O nama</h2>
    <Row className="g-5">
      <Col md={8}>
        <h4 className="mb-3">MEN-ING SISTEM DOO, Smederevo</h4>
        <p className="lead">
          Naša štamparija već dugi niz godina ponosno proizvodi kartonsku ambalažu i blokovsku robu
          za klijente iz cele Srbije.
        </p>
        <p>
          U Smederevu je nekada postojalo <strong>12 štamparija</strong>. Vremenom, kako se tržište menjalo,
          mnoge su zatvorile vrata. Danas su <strong>preostale samo dve</strong> – jedna od njih smo mi.
          To nije slučajno: kvalitet, posvećenost klijentima i prilagodljivost potrebama svake porudžbine
          razlog su što i danas pravimo proizvode za poznate brendove i male preduzetnike.
        </p>
        <h5 className="mt-4">Šta proizvodimo</h5>
        <ul>
          <li>Kartonske kutije za roštilj, sa štampom po želji klijenta</li>
          <li>Srebrne kutije za pomfrit, čevape i toplu hranu sa aluminijumskom oblogom</li>
          <li>Kutije za kolače sa providnim prozorom (kvadratne i pravougaone)</li>
          <li>Papirne i kartonske podmetače za piće</li>
          <li>Zidne i stone kalendare</li>
          <li>Blokovska roba: otpremnice, računi, reversi (NCR papir)</li>
        </ul>
        <h5 className="mt-4">Načini dostave</h5>
        <ul>
          <li><i className="bi bi-shop me-2 text-brand"></i>Lično preuzimanje u našem pogonu u Smederevu</li>
          <li><i className="bi bi-truck me-2 text-brand"></i>Dostava na vašu adresu</li>
          <li><i className="bi bi-box-seam me-2 text-brand"></i>Slanje kurirskim službama širom Srbije</li>
        </ul>
      </Col>
      <Col md={4}>
        <div className="p-4 bg-white rounded shadow-sm">
          <h5 className="mb-3">Zašto baš mi?</h5>
          <div className="mb-3">
            <i className="bi bi-award-fill text-brand fs-3"></i>
            <h6 className="mt-2 mb-1">Tradicija</h6>
            <small className="text-muted">Decenijama u poslu, opstali smo zato što radimo dobro.</small>
          </div>
          <div className="mb-3">
            <i className="bi bi-people-fill text-brand fs-3"></i>
            <h6 className="mt-2 mb-1">Individualni pristup</h6>
            <small className="text-muted">Svaka porudžbina je posebna, savetujemo i prilagođavamo.</small>
          </div>
          <div className="mb-3">
            <i className="bi bi-lightning-fill text-brand fs-3"></i>
            <h6 className="mt-2 mb-1">Brza isporuka</h6>
            <small className="text-muted">Realni rokovi koje uvek ispoštujemo.</small>
          </div>
          <div>
            <i className="bi bi-cash-coin text-brand fs-3"></i>
            <h6 className="mt-2 mb-1">Konkurentne cene</h6>
            <small className="text-muted">Bez kompromisa po kvalitetu.</small>
          </div>
        </div>
      </Col>
    </Row>
  </Container>
);

export default AboutPage;
