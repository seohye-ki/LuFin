import { Hero } from './components/Hero';
import { PrimaryFeatures } from './components/PrimaryFeatures';
import { CallToAction } from './components/CallToAction';
import { Header } from './components/Header';
import { Footer } from './components/Footer';

function Home() {
  return (
    <>
      <Header />
      <main>
        <Hero />
        <PrimaryFeatures />
        <CallToAction />
      </main>
      <Footer />
    </>
  );
}

export default Home;
